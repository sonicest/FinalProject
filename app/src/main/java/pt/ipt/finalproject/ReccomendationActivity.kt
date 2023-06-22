package pt.ipt.finalproject

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pt.ipt.finalproject.databinding.ActivityTasksBinding

data class Recommendation(val name: String, val description: String)

class ReccomendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTasksBinding
    private lateinit var recommendationsListView: ListView
    private lateinit var recommendationDescriptionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recommendationsListView = binding.recommendationsListView
        recommendationDescriptionTextView = binding.recommendationDescriptionTextView

        val recommendations = generateRecommendations()
        val recommendationNames = recommendations.map { it.name }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, recommendationNames)
        recommendationsListView.adapter = adapter

        recommendationsListView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedRecommendation = recommendations[position]
                showInfoWindow(selectedRecommendation)
            }
    }

    private fun showInfoWindow(recommendation: Recommendation) {
        recommendationDescriptionTextView.text = recommendation.description
        recommendationDescriptionTextView.visibility = View.VISIBLE
    }

    private fun generateRecommendations(): List<Recommendation> {
        val recommendations = mutableListOf<Recommendation>()

        recommendations.add(
            Recommendation(
                "Practice self-care",
                "Make self-care a priority by engaging in activities that promote relaxation, such as meditation, deep breathing exercises, taking baths, or pursuing hobbies you enjoy."
            )
        )
        recommendations.add(
            Recommendation(
                "Maintain a balanced lifestyle",
                "Ensure you have a healthy balance between work, leisure, relationships, and personal time. Prioritize activities that bring you joy and allow for mental and emotional rejuvenation."
            )
        )
        recommendations.add(
            Recommendation(
                "Seek social support",
                "Cultivate strong relationships with supportive individuals who can provide emotional support and understanding. Surrounding yourself with positive and caring people can contribute significantly to your mental well-being."
            )
        )

        recommendations.add(
            Recommendation(
                "Develop emotional awareness",
                "Pay attention to your emotions and try to understand their underlying causes. Reflect on your feelings, and consider journaling or talking to a trusted confidant to gain clarity and insight into your emotional state."
            )
        )

        recommendations.add(
            Recommendation(
                "Practice mindfulness",
                "Engage in mindfulness techniques, such as meditation or mindful breathing, to bring your attention to the present moment. This can help reduce stress, enhance self-awareness, and improve emotional regulation."
            )
        )

        recommendations.add(
            Recommendation(
                "Enhance communication skills",
                "Work on improving your communication skills, including active listening, empathy, and assertiveness. Effective communication can foster healthier relationships and minimize conflicts, leading to better emotional well-being."
            )
        )

        recommendations.add(
            Recommendation(
                "Manage stress",
                "Develop strategies to manage stress effectively. This can include practicing relaxation techniques, setting realistic goals, prioritizing tasks, and learning to delegate or seek assistance when needed."
            )
        )

        recommendations.add(
            Recommendation(
                "Set boundaries",
                "Establish clear boundaries to protect your mental and emotional well-being. Learn to say no when necessary, and communicate your needs and limitations to others. Boundaries help prevent burnout and promote healthy relationships."
            )
        )

        recommendations.add(
            Recommendation(
                "Engage in physical activity",
                "Regular exercise has been shown to have positive effects on mental health. Find activities you enjoy, such as walking, cycling, dancing, or yoga, and incorporate them into your routine."
            )
        )

        recommendations.add(
            Recommendation(
                "Seek professional help when needed",
                "If you're experiencing persistent or severe mental health challenges, don't hesitate to seek support from a mental health professional. They can provide guidance, therapy, or medication if necessary."
            )
        )

        recommendations.add(
            Recommendation(
                "Practice empathy",
                "Cultivate empathy by putting yourself in others' shoes and trying to understand their perspectives and emotions. This fosters emotional intelligence and enhances interpersonal relationships."
            )
        )

        recommendations.add(
            Recommendation(
                "Learn from failures and setbacks",
                "View setbacks and failures as opportunities for growth and learning. Practice resilience and develop a positive mindset that allows you to bounce back from challenges."
            )
        )

        recommendations.add(
            Recommendation(
                "Engage in self-reflection",
                "Set aside regular time for self-reflection to assess your emotions, behaviors, and thought patterns. This self-awareness enables you to make positive changes and develop emotional intelligence."
            )
        )

        recommendations.add(
            Recommendation(
                "Cultivate gratitude",
                "Practice gratitude by regularly expressing appreciation for the positive aspects of your life. This can help shift your focus toward the good, enhance your overall well-being, and improve your emotional intelligence."
            )
        )

        recommendations.add(
            Recommendation(
                "Take breaks and practice relaxation",
                "Incorporate regular breaks and moments of relaxation into your daily routine. Engaging in activities such as deep breathing, progressive muscle relaxation, or listening to calming music can help reduce stress and promote emotional well-being."
            )
        )

        return recommendations
    }
}