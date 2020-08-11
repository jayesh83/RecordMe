package com.japps.recordme

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


class FirstFragment : Fragment() {
    private var mediaPlayer: MediaPlayer? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        view.findViewById<Button>(R.id.button_record).setOnClickListener {
            val intent = Intent(requireContext(), RecorderService::class.java)
//            requireActivity().startService(intent)

            ContextCompat.startForegroundService(requireContext(), intent)
        }

        view.findViewById<Button>(R.id.button_play).setOnClickListener {
            val outputFile =
                "${requireActivity().externalCacheDir?.absolutePath}/audiorecordtest.amr"
            try {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(outputFile)
                    prepare()
                    start()
                }
            } catch (e: Exception) {
                Log.e("Player", "Error -> $e")
                Toast.makeText(requireContext().applicationContext, "Error", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        view.findViewById<Button>(R.id.button_pause).setOnClickListener {
            try {
                if (mediaPlayer?.isPlaying!!) {
                    mediaPlayer?.stop()
                }
            } catch (exe: ExceptionInInitializerError) {
                Log.e("Stop", exe.stackTrace.toString())
            } catch (generalException: Exception) {
                Log.e("Stop", generalException.stackTrace.toString())
            }
        }

        view.findViewById<Button>(R.id.button_stop_record).setOnClickListener {
            val intent = Intent(requireContext(), RecorderService::class.java)
            requireActivity().stopService(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("FirstFragment", "Fragment got killed")
    }
}