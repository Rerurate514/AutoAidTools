package com.example.autoaidtools

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import com.example.autoaidtools.databinding.FragmentControlBinding

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ControlFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ControlFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentControlBinding? = null
    private val binding get() = _binding!!

    private lateinit var thisAc : Context

    private val viewModel : ViewModel by activityViewModels()

    private val listPos = mAPI.countClosure()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentControlBinding.inflate(inflater, container, false)
        return binding.root
        //return inflater.inflate(R.layout.fragment_control, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        thisAc = requireActivity()

        binding.overlayShowButton.setOnClickListener { (activity as? MainActivity)?.openOverlay() }
        binding.autoTapButton.setOnClickListener { autoTapButtonTap() }
        binding.delayButton.setOnClickListener { secondDelayButtonTap() }
    }

    private fun autoTapButtonTap(){
        viewModel.addCycleList(1)
        addImgViewInTable(R.drawable.aats_control_layout_tapimage,"  auto 1 tap  ",listPos())
        addViewGrayLineInTable()
    }

    private fun secondDelayButtonTap(){
        enterDelayTimeInDialog()
    }

    private fun delayViewDisplay(delayTime: Int){
        viewModel.addCycleList(2,delayTime)
        addImgViewInTable(R.drawable.aats_control_layout_delayimage,"delay $delayTime second",listPos())
        addViewGrayLineInTable()
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    private fun addImgViewInTable(imgId : Int, text: String = "", index: Int){
        val btmParams = ViewGroup.LayoutParams(60,60)
        val tblParams = ViewGroup.LayoutParams(WRAP_CONTENT,200)
        val imgView = ImageView(thisAc).also{
            it.setImageResource(imgId)
            it.scaleType = ImageView.ScaleType.FIT_CENTER
        }

        val txtView = TextView(thisAc).also{
            it.text = "\n${text}"
            it.textSize = 17f
        }

        val cardView = CardView(thisAc).also{
            it.radius = 20f
        }

        val btm = ImageButton(thisAc).also{
            it.setImageResource(R.drawable.aats_control_layout_removeview)
            it.layoutParams = btmParams
            it.scaleType = ImageView.ScaleType.FIT_CENTER

            it.setBackgroundColor(Color.GRAY)

            it.setOnTouchListener { _, event ->
                when(event.action){
                    MotionEvent.ACTION_BUTTON_PRESS -> {
                        //removeViewFromTable()
                        true
                    }
                    MotionEvent.ACTION_DOWN -> {
                        it.alpha = 0.5f
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        it.alpha = 1f
                        false
                    }
                    else -> false
                }
            }
        }

        val space1 = Space(thisAc)
        val space2 = Space(thisAc)

        val tableRow = TableRow(thisAc).also{
            it.layoutParams = tblParams
        }

        binding.addViewLayout.addView(tableRow)

        tableRow.addView(imgView,200,210)
        tableRow.addView(space1,100,210)
        tableRow.addView(txtView,400,210)
        tableRow.addView(space2,110,210)
        tableRow.addView(cardView, 200, 200)

        cardView.addView(btm, WRAP_CONTENT, WRAP_CONTENT)
    }

    private fun addViewGrayLineInTable(){
        val tblParams = ViewGroup.LayoutParams(WRAP_CONTENT,200)
        val grayLine = ImageView(thisAc).also{
            it.setImageResource(R.drawable.aats_control_layout_glayline)
            it.scaleType = ImageView.ScaleType.FIT_CENTER
        }

        val space = Space(thisAc)

        val tableRow = TableRow(thisAc).also{
            it.layoutParams = tblParams
        }

        binding.addViewLayout.addView(tableRow)

        tableRow.addView(grayLine, 20,200)
        tableRow.addView(space,20,210)
    }

    private fun removeViewFromTable(index: Int){
        viewModel.removeCycleList(index)
        redrawList()
    }

    private fun redrawList(){
        binding.addViewLayout.removeAllViews()
        for(i in viewModel.doAutoCycleList){
            when(viewModel.doAutoCycleList[i]){
                viewModel.oneTap -> {
                    autoTapButtonTap()
                }
                viewModel.nDelay -> {
                    delayViewDisplay(viewModel.delayTimeList[i])
                }
            }
        }
    }

    private fun enterDelayTimeInDialog(){
        val editText = EditText(thisAc)
        var delayTime = 0

        AlertDialog.Builder(thisAc).apply{
            this.setTitle("enter delay time")
            this.setView(editText)
            this.setPositiveButton("ok") { dialog,_ ->
                val text = editText.text.toString()
                delayTime = text.toIntOrNull() ?: 0

                when(delayTime){
                    0 -> {
                        val toast = Toast.makeText(
                            thisAc,
                            "Please enter greater delay time than 0 or not blank",
                            Toast.LENGTH_SHORT
                        )
                        toast.setGravity(Gravity.BOTTOM,0,100)
                        toast.show()
                        enterDelayTimeInDialog()
                    }
                    else -> {
                        dialog.dismiss()

                        delayViewDisplay(delayTime)
                    }
                }
            }
            this.setNegativeButton("back"){ dialog,_ ->
                dialog.dismiss()
            }
            this.show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ControlFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ControlFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
