package com.example.list_mail

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmailAdapter(private val allEmails: MutableList<Email>, private val onItemClick: (Email, Int) -> Unit) : RecyclerView.Adapter<EmailAdapter.EmailVH>() {

    private val shownEmails: MutableList<Email> = allEmails.toMutableList()
    private enum class Filter { ALL, FAV, RECENT }
    private var currentFilter: Filter = Filter.ALL

    inner class EmailVH(view: View) : RecyclerView.ViewHolder(view) {
        val tvAvatar: TextView = view.findViewById(R.id.tvAvatar)
        val tvSender: TextView = view.findViewById(R.id.tvSender)
        val tvSubject: TextView = view.findViewById(R.id.tvSubject)
        val tvPreview: TextView = view.findViewById(R.id.tvPreview)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val ivStar: ImageView = view.findViewById(R.id.ivStar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_email, parent, false)
        return EmailVH(v)
    }

    override fun onBindViewHolder(holder: EmailVH, position: Int) {
        val email = shownEmails[position]
        holder.tvAvatar.text = email.initial
        // Dynamic color circle
        val bg = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(randomColorFor(email.initial))
        }
        holder.tvAvatar.background = bg
        holder.tvSender.text = email.senderName
        holder.tvSubject.text = email.subject
        holder.tvPreview.text = email.preview
        holder.tvTime.text = email.time
        holder.ivStar.setImageResource(if (email.starred) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off)
        holder.ivStar.setOnClickListener {
            email.starred = !email.starred
            // Nếu đang ở favorites mà bỏ sao -> refresh
            applyFilter()
        }
        holder.itemView.setOnClickListener {
            onItemClick(email, position)
        }
    }

    override fun getItemCount(): Int = shownEmails.size

    fun filter(query: String?) {
        val q = query?.trim()?.lowercase().orEmpty()
        shownEmails.clear()
        if (q.isEmpty()) {
            shownEmails.addAll(allEmails)
        } else {
            shownEmails.addAll(allEmails.filter {
                it.senderName.lowercase().contains(q) ||
                        it.subject.lowercase().contains(q) ||
                        it.preview.lowercase().contains(q)
            })
        }
        notifyDataSetChanged()
    }

    fun showInbox() { currentFilter = Filter.ALL; applyFilter() }
    fun showFavorites() { currentFilter = Filter.FAV; applyFilter() }
    fun showRecent() { currentFilter = Filter.RECENT; applyFilter() }

    private fun applyFilter() {
        shownEmails.clear()
        when (currentFilter) {
            Filter.ALL -> shownEmails.addAll(allEmails)
            Filter.FAV -> shownEmails.addAll(allEmails.filter { it.starred })
            Filter.RECENT -> shownEmails.addAll(allEmails.take(5))
        }
        notifyDataSetChanged()
    }

    private fun randomColorFor(seed: String): Int {
        val colors = listOf("#F44336", "#E91E63", "#9C27B0", "#3F51B5", "#03A9F4", "#009688", "#4CAF50", "#FF9800")
        val idx = (seed.first().code + seed.length) % colors.size
        return Color.parseColor(colors[idx])
    }
}
