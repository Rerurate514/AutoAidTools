package com.example.autoaidtools

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.example.autoaidtools.databinding.FragmentOverlayBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OverlayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OverlayFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentOverlayBinding? = null
    private val binding get() = _binding!!

    private var mActivity: AppCompatActivity? = null

    private lateinit var windowManager: WindowManager

    private var overlayView : View? = null

    private val viewModel : ViewModel by activityViewModels()

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
        _binding = FragmentOverlayBinding.inflate(inflater, container, false)
        return binding.root
        //return inflater.inflate(R.layout.fragment_control, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        overlayView = binding.overlayContainer

        windowManager = requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        overlay()

        binding.overlayButton.setOnClickListener { fragmentFinish() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AppCompatActivity) mActivity = context
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    @SuppressLint("InflateParams")
    private fun fragmentFinish(){
        windowManager.removeView(overlayView)
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun overlay() {
        val mFragment = OverlayFragment()
        if(!mFragment.isAdded && overlayView != null) {
            showAsOverlay(overlayView!!)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showAsOverlay(view: View) {
        val windowManager = view.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        val parentView = view.parent as? ViewGroup
        parentView?.removeView(view) // 親から削除する
        windowManager.addView(view, layoutParams)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OverlayFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OverlayFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}