package com.example.tbcacademyfinal.presentation.ui.main.model_scene

import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.common.safecalls.CollectSideEffect
import com.example.tbcacademyfinal.presentation.theme.GreenLinearGradient
import com.example.tbcacademyfinal.presentation.theme.PlainWhite
import io.github.sceneview.Scene
import io.github.sceneview.animation.Transition.animateRotation
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import io.github.sceneview.rememberOnGestureListener
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit.MILLISECONDS

@Composable
fun ModelScreen(
    viewModel: ModelViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    CollectSideEffect(flow = viewModel.event) {
        when (it) {
            ModelSideEffect.NavigateBack -> onNavigateBack()
            is ModelSideEffect.ShowError -> TODO()
        }
    }

    ModelContent(
        state = viewModel.state,
        onIntent = viewModel::processIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelContent(
    state: ModelState,
    onIntent: (ModelIntent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        state.product?.name ?: stringResource(R.string.details_title)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onIntent(ModelIntent.ClickedBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.background(GreenLinearGradient)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val engine = rememberEngine()
            val modelLoader = rememberModelLoader(engine)
            val environmentLoader = rememberEnvironmentLoader(engine)

            val centerNode = rememberNode(engine)

            val cameraNode = rememberCameraNode(engine) {
                position = Position(y = -0.5f, z = 2.0f)
                lookAt(centerNode)
                centerNode.addChildNode(this)
            }

            val cameraTransition = rememberInfiniteTransition(label = "CameraTransition")
            val cameraRotation by cameraTransition.animateRotation(
                initialValue = Rotation(y = 0.0f),
                targetValue = Rotation(y = 360.0f),
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 7.seconds.toInt(MILLISECONDS))
                )
            )
            if (state.isLoading && state.product == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Scene(
                    modifier = Modifier.fillMaxSize(),
                    engine = engine,
                    modelLoader = modelLoader,
                    cameraNode = cameraNode,
                    cameraManipulator = rememberCameraManipulator(
                        orbitHomePosition = cameraNode.worldPosition,
                        targetPosition = centerNode.worldPosition
                    ),
                    childNodes = listOf(centerNode,
                        rememberNode {
                            ModelNode(
                                modelInstance = modelLoader.createModelInstance(
                                    assetFileLocation = state.product?.modelFile ?: ""
                                ),
                                scaleToUnits = 0.25f
                            )
                        }),
                    environment = environmentLoader.createHDREnvironment(
                        assetFileLocation = "environments/sky_2k.hdr"
                    )!!,
                    onFrame = {
                        centerNode.rotation = cameraRotation
                        cameraNode.lookAt(centerNode)
                    },
                    onGestureListener = rememberOnGestureListener(
                        onDoubleTap = { _, node ->
                            node?.apply {
                                scale *= 2.0f
                            }
                        },
                        onSingleTapConfirmed = { _, node ->
                            node?.apply {
                                scale /= 2.0f
                            }
                        },
                    )
                )
            }
        }
    }
}