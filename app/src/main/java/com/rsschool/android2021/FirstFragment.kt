package com.rsschool.android2021

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class FirstFragment : Fragment() {

    private var generateButton: Button? = null
    private var previousResult: TextView? = null
    private lateinit var minValue: EditText
    private lateinit var maxValue: EditText

    private lateinit var fragmentSendRangeDataListener: OnFragmentSendRangeData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        previousResult = view.findViewById(R.id.previous_result)
        generateButton = view.findViewById(R.id.generate)
        minValue = view.findViewById(R.id.min_value)
        maxValue = view.findViewById(R.id.max_value)

        val result = arguments?.getInt(PREVIOUS_RESULT_KEY)
        previousResult?.text = "Previous result: ${result.toString()}"

        generateButton?.setOnClickListener {
            val min = minValue.text.toString()
            val max = maxValue.text.toString()
            if (validateData(min, max)) {
                fragmentSendRangeDataListener.sendRangeData(min.toInt(), max.toInt())
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(previousResult: Int): FirstFragment {
            val fragment = FirstFragment()
            val args = Bundle()
            args.putInt(PREVIOUS_RESULT_KEY, previousResult)
            fragment.arguments = args
            return fragment
        }

        private const val PREVIOUS_RESULT_KEY = "PREVIOUS_RESULT"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentSendRangeDataListener = context as OnFragmentSendRangeData
    }

    interface OnFragmentSendRangeData {
        fun sendRangeData(min: Int, max: Int)
    }

    private fun validateData(min: String, max: String): Boolean {
        return if (min == "" || max == "") {
            Toast.makeText(context, "Please, input values", Toast.LENGTH_SHORT).show()
            false
        } else if (min[0] == '-' || max[0] == '-') {
            Toast.makeText(context, "Error, values should be positive or '0'", Toast.LENGTH_LONG)
                .show()
            false
        } else if (min.toIntOrNull() == null || max.toIntOrNull() == null) {
            Toast.makeText(
                context,
                "Error, values should be in range [0..2147483647]",
                Toast.LENGTH_LONG
            ).show()
            false
        } else if (min.toInt() > max.toInt()) {
            Toast.makeText(context, "Error, min should not be bigger than max", Toast.LENGTH_LONG)
                .show()
            false
        } else {
            true
        }
    }
}