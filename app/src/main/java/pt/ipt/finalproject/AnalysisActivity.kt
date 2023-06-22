package pt.ipt.finalproject

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import pt.ipt.finalproject.databinding.ActivityAnalysisBinding
import pt.ipt.finalproject.utilities.Constant.Companion.helper
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter
import pt.ipt.finalproject.models.Emotions

class AnalysisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnalysisBinding
    private lateinit var chart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showChart()
        setListener()
    }

    private fun showChart() {
        chart = binding.chart

        // Configure the chart
        chart.description.isEnabled = false
        chart.setDrawGridBackground(false)
        chart.setPinchZoom(false)
        chart.setScaleEnabled(false)
        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)
        chart.setMaxVisibleValueCount(50)
        chart.setDrawGridBackground(false)
        chart.setDrawBorders(false)
        chart.animateY(1000)

        val emotions = helper.getEmotions()


        // Create the bar entries


        val entries = mutableListOf<BarEntry>()

        var x = 0f
        var y = 0f

        val averages = calculateAverageByDay(emotions)

        val resultMap = mutableMapOf<Float, Float>()
        for (i in 1..7) {
            if (averages.containsKey(i.toFloat())) {
                resultMap[i.toFloat()] = averages[i.toFloat()]!!
            } else {
                resultMap[i.toFloat()] = 0.0f
            }
        }

        for ((day, average) in resultMap) {
            x = day
            y = average
            println("$day: $average")
            entries.add(BarEntry(x, y))
        }
        // Create the bar dataset
        val dataSet = BarDataSet(entries, "Data")

        dataSet.color = ContextCompat.getColor(this, R.color.chart)
        dataSet.valueTextColor = ContextCompat.getColor(this, android.R.color.black)
        dataSet.setDrawValues(false)
        // Create the bar data
        val barData = BarData(dataSet)

        barData.barWidth = 0.7f

        chart.description.isEnabled = false
        chart.setDrawGridBackground(false)
        chart.setPinchZoom(false)
        chart.setScaleEnabled(false)
        chart.setDrawBarShadow(true)
        chart.setDrawValueAboveBar(false)
        chart.setMaxVisibleValueCount(50)
        chart.setDrawGridBackground(false)
        chart.setDrawBorders(false)
        chart.animateY(1000)
        chart.apply {
            data = barData
           // xAxis.position = XAxis.XAxisPosition.BOTTOM
           // description.isEnabled = true
            xAxis.valueFormatter =
               IndexAxisValueFormatter(arrayOf( "Sun","Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"))


            // axisLeft.valueFormatter = createCustomYAxisValueFormatter(context, drawableIds)
            axisLeft.axisMinimum = 0f
            axisLeft.axisMaximum = 5f
            axisLeft.labelCount = 5
            axisLeft.setDrawLabels(false)

            axisLeft.setDrawGridLines(false)
            xAxis.setDrawGridLines(false)
            xAxis.granularity = 1f // Interval between labels
            xAxis.labelCount = 7 // Number of labels
            axisRight.isEnabled = false
            legend.isEnabled = false
            invalidate()
        }
    }

    fun calculateAverageByDay(numbersAndDays: ArrayList<Emotions>): Map<Float, Float> {
        val result = mutableMapOf<Float, Float>()
        val counts = mutableMapOf<Float, Int>()
        val sums = mutableMapOf<Float, Float>()

        val dayMapping = mapOf(

            "Monday" to 1F,
            "Tuesday" to 2F,
            "Wednesday" to 3F,
            "Thursday" to 4F,
            "Friday" to 5F,
            "Saturday" to 6F,
            "Sunday" to 7F
        )

        for (pair in numbersAndDays) {
            val number = pair.type.toFloat()
            val day = pair.date

            val dayNumber = dayMapping[day] ?: continue

            counts[dayNumber] = (counts[dayNumber] ?: 0) + 1
            sums[dayNumber] = (sums[dayNumber] ?: 0F) + number
        }

        for (day in counts.keys) {
            val count = counts[day] ?: 0
            val sum = sums[day] ?: 0F
            val average = if (count > 0) sum / count.toFloat() else 0.0f
            result[day] = average
        }

        return result
    }

    private fun setListener() {
        binding.rec.setOnClickListener {
            Intent(applicationContext, ReccomendationActivity::class.java).also {
                startActivity(it)
            }
        }
    }

}