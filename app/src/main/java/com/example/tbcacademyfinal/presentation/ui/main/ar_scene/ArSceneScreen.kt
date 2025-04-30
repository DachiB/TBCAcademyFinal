package com.example.tbcacademyfinal.presentation.ui.main.ar_scene

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.PixelCopy
import android.view.SurfaceView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.common.safecalls.CollectSideEffect
import com.example.tbcacademyfinal.presentation.ui.main.ar_scene.components.CollectionThumbnail
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.TrackingFailureReason
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.node.CubeNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ArSceneScreen(
    viewModel: ArSceneViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    CollectSideEffect(flow = viewModel.event) {
        when (it) {
            is ArSceneSideEffect.ShowSnackBar -> snackbarHostState.showSnackbar(
                message = it.message,
                actionLabel = "Dismiss",
                withDismissAction = true
            )

            ArSceneSideEffect.NavigateBack -> onNavigateBack()
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }) { _ ->
        ArScreenContent(
            state = viewModel.state,
            onIntent = viewModel::processIntent
        )
    }
}


@Composable
fun ArScreenContent(
    state: ArSceneState,
    onIntent: (ArSceneIntent) -> Unit = {},
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val engine = rememberEngine()
        val modelLoader = rememberModelLoader(engine)
        val materialLoader = rememberMaterialLoader(engine)
        val cameraNode = rememberARCameraNode(engine)
        val childNodes = rememberNodes()
        val view = rememberView(engine)
        val surfaceView = SurfaceView(context)
        val collisionSystem = rememberCollisionSystem(view)

        var planeRenderer by remember { mutableStateOf(true) }

        var trackingFailureReason by remember {
            mutableStateOf<TrackingFailureReason?>(null)
        }
        var frame by remember { mutableStateOf<Frame?>(null) }

        var currentModelFile by remember { mutableStateOf("models/lamp.glb") }

        ARScene(
            modifier = Modifier.fillMaxSize(),
            childNodes = childNodes,
            engine = engine,
            view = view,
            modelLoader = modelLoader,
            collisionSystem = collisionSystem,
            sessionConfiguration = { session, config ->
                config.depthMode =
                    when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                        true -> Config.DepthMode.AUTOMATIC
                        else -> Config.DepthMode.DISABLED
                    }
                config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
                config.lightEstimationMode =
                    Config.LightEstimationMode.ENVIRONMENTAL_HDR
            },
            cameraNode = cameraNode,
            planeRenderer = planeRenderer,
            onTrackingFailureChanged = {
                trackingFailureReason = it
            },
            onSessionUpdated = { _, updatedFrame ->
                frame = updatedFrame

                if (childNodes.isEmpty()) {
                    updatedFrame.getUpdatedPlanes()
                        .firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
                        ?.let { it.createAnchorOrNull(it.centerPose) }?.let { anchor ->
                            childNodes += createAnchorNode(
                                engine = engine,
                                modelLoader = modelLoader,
                                materialLoader = materialLoader,
                                anchor = anchor,
                                currentModelFile = currentModelFile
                            )
                        }
                }
            },
            onGestureListener = rememberOnGestureListener(
                onSingleTapConfirmed = { motionEvent, node ->
                    if (node == null) {
                        val hitResults = frame?.hitTest(motionEvent.x, motionEvent.y)
                        hitResults?.firstOrNull {
                            it.isValid(
                                depthPoint = false,
                                point = false
                            )
                        }?.createAnchorOrNull()
                            ?.let { anchor ->
                                planeRenderer = false
                                childNodes += createAnchorNode(
                                    engine = engine,
                                    modelLoader = modelLoader,
                                    materialLoader = materialLoader,
                                    anchor = anchor,
                                    currentModelFile = currentModelFile
                                )
                            }
                    }
                })
        )

        FloatingActionButton(
            onClick = {
                captureArView(
                    view = surfaceView,
                    context = context,
                    coroutineScope = coroutineScope,
                    onBitmapReady = { bitmap -> onIntent(ArSceneIntent.PhotoCaptured(bitmap)) },
                    onError = { errorMsg ->
                        Log.e("ArScreen", "Capture Error: $errorMsg")
                    }
                )
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .systemBarsPadding()
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.tertiaryContainer // Example color
        ) {
            Icon(
                Icons.Filled.AddCircle,
                contentDescription = stringResource(R.string.take_photo) // Add string
            )
        }

        IconButton(
            onClick = { onIntent(ArSceneIntent.GoBackClicked) },
            modifier = Modifier
                .align(Alignment.TopStart)
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }


        Text(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(
                    top = 16.dp,
                    start = 72.dp,
                    end = 72.dp
                ),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.White,
            text = state.instructionText
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .systemBarsPadding() // Adjust for navigation bar
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(vertical = 8.dp)
        ) {
            if (state.isLoadingCollection) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.collectionError != null) {
                Text(
                    "Error: ${state.collectionError}",
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(8.dp)
                )
            } else if (state.availableItems.isEmpty()) {
                Text(
                    "Collection is empty.",
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(8.dp)
                )
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(state.availableItems, key = { it.productId }) { item ->
                        CollectionThumbnail(
                            item = item,
                            isSelected = state.selectedItemModelFile == item.modelFile,
                            onClick = {
                                Log.d(
                                    "ArScreen",
                                    "Clicked item: ${item.name}, Model: ${item.modelFile}"
                                )
                                currentModelFile = item.modelFile
                            }
                        )
                    }
                }
            }
        }
    }
}

fun captureArView(
    view: SurfaceView,
    context: Context,
    coroutineScope: kotlinx.coroutines.CoroutineScope,
    onBitmapReady: (Bitmap) -> Unit, // Callback for success
    onError: (String) -> Unit // Callback for error
) {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    PixelCopy.request(view, bitmap, { copyResult ->
        if (copyResult == PixelCopy.SUCCESS) {
            // Don't save here, pass bitmap back to ViewModel via callback
            coroutineScope.launch(Dispatchers.Main) { // Ensure callback on main thread
                onBitmapReady(bitmap)
            }
        } else {
            coroutineScope.launch(Dispatchers.Main) {
                onError("PixelCopy Error: $copyResult")
            }
        }
    }, android.os.Handler(context.mainLooper))
}

fun createAnchorNode(
    engine: Engine,
    modelLoader: ModelLoader,
    materialLoader: MaterialLoader,
    anchor: Anchor,
    currentModelFile: String = "models/lamp.glb"
): AnchorNode {
    val anchorNode = AnchorNode(engine = engine, anchor = anchor)
    val modelNode = ModelNode(
        modelInstance = modelLoader.createModelInstance(currentModelFile),
        // Scale to fit in a 0.5 meters cube
        scaleToUnits = 0.5f
    ).apply {
        // Model Node needs to be editable for independent rotation from the anchor rotation
        isEditable = true
        isScaleEditable = true
        editableScaleRange = 0.2f..0.75f
    }
    val boundingBoxNode = CubeNode(
        engine,
        size = modelNode.extents,
        center = modelNode.center,
        materialInstance = materialLoader.createColorInstance(Color.White.copy(alpha = 0.5f))
    ).apply {
        isVisible = false
    }
    modelNode.addChildNode(boundingBoxNode)
    anchorNode.addChildNode(modelNode)

    listOf(modelNode, anchorNode).forEach {
        it.onEditingChanged = { editingTransforms ->
            boundingBoxNode.isVisible = editingTransforms.isNotEmpty()
        }
    }
    return anchorNode
}