package com.ngoclong.mfa_libvlc


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var mLibVLC: LibVLC
    private lateinit var mMediaPlayer: MediaPlayer

    companion object {
        private val USE_TEXTURE_VIEW = false
        private val ENABLE_SUBTITLES = true
        private val ASSET_FILENAME = "bbb.m4v"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val args = ArrayList<String>()
        args.add("-vvv")
        mLibVLC = LibVLC(this, args)
        mMediaPlayer = MediaPlayer(mLibVLC)
    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaPlayer.release()
        mLibVLC.release()
    }

    override fun onStart() {
        super.onStart()

        mMediaPlayer.attachViews(mVideoLayout, null, ENABLE_SUBTITLES, USE_TEXTURE_VIEW)

        try {
            val media = Media(mLibVLC, assets.openFd(ASSET_FILENAME))
            mMediaPlayer.setMedia(media)
            media.release()
        } catch (e: IOException) {
            Toast.makeText(applicationContext, "File not found!!", Toast.LENGTH_LONG).show()
            throw RuntimeException("Invalid asset folder")
        }

        mMediaPlayer.play()
    }

    override fun onStop() {
        super.onStop()

        mMediaPlayer.stop()
        mMediaPlayer.detachViews()
    }
}
