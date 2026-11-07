package com.healthku.app.ui.asclepios

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun openUrl(ctx: Context, url: String) {
    try { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
    catch (e: Exception) { Toast.makeText(ctx, "Tidak ada aplikasi untuk membuka tautan", Toast.LENGTH_SHORT).show() }
}

fun sendEmail(ctx: Context, to: String, subject: String) {
    try {
        ctx.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$to?subject=${Uri.encode(subject)}")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    } catch (e: Exception) { Toast.makeText(ctx, "Tidak ada aplikasi email", Toast.LENGTH_SHORT).show() }
}

fun shareText(ctx: Context, text: String) {
    val send = Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_TEXT, text) }
    ctx.startActivity(Intent.createChooser(send, "Bagikan").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}
