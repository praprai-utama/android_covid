package com.codemobiles.myapp.ui.main

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codemobiles.myapp.R
import com.codemobiles.myapp.databinding.FragmentChartBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.google.gson.Gson
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ChartFragment : Fragment() {




    private lateinit var binding: FragmentChartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChartBinding.inflate(inflater, container, false)
        setUpEventWidget()
        drawChart(0)

        return binding.root
    }

    private fun setUpEventWidget() {
        binding.segmentedSliver.setOnClickListener {
            drawChart(0)
        }
        binding.segmentedGold.setOnClickListener {
            drawChart(1)
        }
        binding.segmentedPlatinum.setOnClickListener {
            drawChart(2)
        }
    }

    private fun drawChart(flag: Int) {
        val result: ChartLineResult = if (flag % 2 == 0) {
            Gson().fromJson(
                getString(R.string.dummy_data_chart_line_1),
                ChartLineResult::class.java
            )
        } else {
            Gson().fromJson(
                getString(R.string.dummy_data_chart_line_2),
                ChartLineResult::class.java
            )
        }

        // set data
        val yVals: ArrayList<Entry> = ArrayList<Entry>()
        val xVals = ArrayList<String>()
        val priceArray: List<ChartLineResult.DataBean>? = result.data
        for (i in priceArray!!.indices) {
            val price: Int = priceArray[i].prices
            val date: String = priceArray[i].date ?: "-"
            yVals.add(Entry(price.toFloat(), i))
            xVals.add(date)
        }
        val dataSet = LineDataSet(yVals, "Dummy Data")
        dataSet.color = Color.parseColor("#E91E63")
        dataSet.lineWidth = 4f
        dataSet.setDrawFilled(true)
        dataSet.fillColor = Color.parseColor("#E91E63")
        dataSet.fillAlpha = 40 //0-100
        dataSet.setDrawCircleHole(true)
        dataSet.setDrawCircles(true)
        dataSet.setCircleColor(Color.parseColor("#DDDDDD"))
        dataSet.circleRadius = 3f
        dataSet.valueTextSize = 15f
        dataSet.valueTypeface = Typeface.DEFAULT
        dataSet.valueTextColor = Color.parseColor("#FBFBFB")

        // Main
        binding.chart.setDescription("SiamGold App.")
        binding.chart.setDescriptionPosition(300f, 50f)
        binding.chart.setDescriptionTextSize(17f)
        binding.chart.setDescriptionColor(Color.parseColor("#FFFFFF"))
        binding.chart.setBackgroundColor(Color.parseColor("#252934"))
        binding.chart.setExtraOffsets(-5f, 50f, 5f, 10f)

        // animation
        //binding.chart.animateXY(1000,1000, Easing.Linear, Easing.Linear);
        binding.chart.animateX(1500, Easing.EasingOption.EaseInCubic)

        // left
        binding.chart.axisLeft.xOffset = 10f
        binding.chart.axisLeft.setDrawLabels(true)
        binding.chart.axisLeft.setDrawAxisLine(false)
        binding.chart.axisLeft.textColor = Color.parseColor("#FFFFFF")
        binding.chart.axisLeft.gridColor = Color.parseColor("#8D8A9A")

        // right
        binding.chart.axisRight.setDrawLabels(false)
        binding.chart.axisRight.setDrawAxisLine(false)

        // bottom
        binding.chart.xAxis.yOffset = 10f
        binding.chart.xAxis.setDrawLabels(true)
        binding.chart.xAxis.textColor = Color.parseColor("#FFFFFF")
        binding.chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.chart.xAxis.gridColor = Color.parseColor("#9d9272")
        binding.chart.xAxis.setDrawGridLines(false)
        binding.chart.xAxis.setDrawAxisLine(false)
        binding.chart.legend.isEnabled = true
        binding.chart.legend.textColor = Color.parseColor("#FFFFFF")
        binding.chart.legend.textSize = 15f

        // create a data object with the data sets
        val data = LineData(xVals, dataSet)
        data.setValueTextColor(Color.WHITE)
        data.setValueTextSize(9f)
        data.setValueFormatter(MyValueFormatter())

        binding.chart.data = data
    }


    private class MyValueFormatter : ValueFormatter {
        override fun getFormattedValue(
            value: Float,
            entry: Entry,
            dataSetIndex: Int,
            viewPortHandler: ViewPortHandler
        ): String {
            val result =
                NumberFormat.getNumberInstance(Locale.US)
                    .format(value.toDouble())
            return "$$result"
        }
    }

    private class ChartLineResult {
        var data: List<DataBean>? = null

        class DataBean {
            var date: String? = null
            var prices = 0
        }
    }



}