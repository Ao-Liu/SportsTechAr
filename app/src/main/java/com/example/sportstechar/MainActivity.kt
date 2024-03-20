package com.example.sportstechar

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.ar.core.Config
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position

class MainActivity : AppCompatActivity() {

    private lateinit var scene: ArSceneView
    private lateinit var goBtn: ExtendedFloatingActionButton
    private lateinit var model: ArModelNode

    private fun placeModel(){
        model.anchor()
        scene.planeRenderer.isVisible = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        scene = findViewById<ArSceneView?>(R.id.sceneView).apply {
            this.lightEstimationMode = Config.LightEstimationMode.DISABLED
        }
        goBtn = findViewById(R.id.go_btn)
        goBtn.setOnClickListener {
            placeModel()
        }
        model = ArModelNode(scene.engine, PlacementMode.INSTANT).apply {
            loadModelGlbAsync(
                glbFileLocation = "models/football.glb",
                scaleToUnits = 1f,
                centerOrigin = Position(-0.5f)
            )
            {
                scene.planeRenderer.isVisible = true
                val materialInstance = it.materialInstances[0]
            }
            onAnchorChanged = {
                goBtn.isGone = it != null
            }
        }
        scene.addChild(model)
    }
}