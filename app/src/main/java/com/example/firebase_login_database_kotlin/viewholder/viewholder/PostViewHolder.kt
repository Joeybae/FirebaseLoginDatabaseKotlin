package com.example.firebase_login_database_kotlin.viewholder.viewholder

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.firebase_login_database_kotlin.R
import com.example.firebase_login_database_kotlin.users.Post
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.include_post_author.view.postAuthor
import kotlinx.android.synthetic.main.include_post_text.view.*
import kotlinx.android.synthetic.main.item_post.view.postNumStars
import kotlinx.android.synthetic.main.item_post.view.star

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindToPost(post: Post, starClickListener: View.OnClickListener) {
        itemView.postTitle.text = post.title
        itemView.postAuthor.text = post.author
        itemView.postNumStars.text = post.starCount.toString()
        itemView.postDate.text = post.date
        itemView.postBody.text = post.body
        itemView.star.setOnClickListener(starClickListener)
    }

    fun setLikedState(liked: Boolean) {
        if (liked) {
            itemView.star.setImageResource(R.drawable.ic_toggle_star_24)
        } else {
            itemView.star.setImageResource(R.drawable.ic_toggle_star_outline_24)
        }
    }

    fun setImage(ctx: Context, IMAGE: String) {
        val post_image = itemView.findViewById<ImageView>(R.id.postImage)
        Picasso.with(ctx)
            .load(IMAGE)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .into(post_image, object : Callback {
                override fun onSuccess() {}

                override fun onError() {
                    //if error occured try again by getting the image from online
                    Picasso.with(ctx)
                        .load(IMAGE)
                        .error(R.drawable.ic_toggle_star_24)
                        .into(post_image, object : Callback {
                            override fun onSuccess() {}

                            override fun onError() {
                                Toast.makeText(ctx, "failed to load image !", Toast.LENGTH_SHORT).show()
                            }
                        })
                }
            })
    }
}
