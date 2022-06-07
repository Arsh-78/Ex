package com.example.counterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.counterapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var count  = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.countButton.setOnClickListener {
            count = count + 1
            binding.textView.text = count.toString()
        }

        binding.resetButton.setOnClickListener {
            count =0
            binding.textView.text = count.toString()
        }

    }
}