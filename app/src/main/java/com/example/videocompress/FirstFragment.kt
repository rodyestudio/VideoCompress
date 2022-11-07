package com.example.videocompress

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.videocompress.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            openCameraToCaptureVideo()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openCameraToCaptureVideo() {
        val packageManager = activity?.packageManager
        if (packageManager != null) {
            if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) { // First check if camera is available in the device
                val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                startActivityForResult(intent, 100);
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            if (intent?.data != null) {
                val uriPathHelper = URIPathHelper()
                val videoFullPath =
                    activity?.let { uriPathHelper.getPath(it, intent.data!!) } // Use this video path according to your logic
                // if you want to play video just after recording it to check is it working (optional)
                if (videoFullPath != null) {
                    playVideoInDevicePlayer(videoFullPath);
                }
            }
        }
    }

    fun playVideoInDevicePlayer(videoPath: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoPath))
        intent.setDataAndType(Uri.parse(videoPath), "video/mp4")
        startActivity(intent)
    }

}