package com.example.tbcacademyfinal.presentation.ui.main.ar_scene

//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.navigation.NavHostController
//import coil3.compose.AsyncImage
//import coil3.request.ImageRequest
//import coil3.request.crossfade
//import com.example.tbcacademyfinal.R
//import com.example.tbcacademyfinal.presentation.model.CollectionItemUi
//import com.example.tbcacademyfinal.presentation.theme.TBCAcademyFinalTheme
//import com.google.android.filament.Engine
//import com.google.ar.core.Anchor
//import com.google.ar.core.Config
//import com.google.ar.core.Frame
//import com.google.ar.core.Plane
//import com.google.ar.core.TrackingFailureReason
//import io.github.sceneview.*
//import io.github.sceneview.loaders.MaterialLoader
//import io.github.sceneview.loaders.ModelLoader
//import io.github.sceneview.math.Position
//import io.github.sceneview.node.CubeNode
//import io.github.sceneview.node.ModelNode

//@Composable
//fun ArSceneScreen(
//    viewModel: ArSceneViewModel = hiltViewModel(),
//    onNavigateBack: () -> Unit
//) {
//    val state by viewModel.state.collectAsStateWithLifecycle()
//    // TODO: Handle side effects like errors if needed
//
//    ArScreenContent(
//        state = state,
//        onIntent = viewModel::processIntent,
//        onTrackingFailureChanged = viewModel::onTrackingFailureChanged, // Pass callback
//        onNavigateBack = { onNavigateBack() }
//    )
//}
//
//
//@Composable
//fun ArScreenContent(
//    state: ArSceneState,
//    onIntent: (ArSceneIntent) -> Unit,
//    onTrackingFailureChanged: (TrackingFailureReason?) -> Unit,
//    onNavigateBack: () -> Unit
//) {
//    Box(modifier = Modifier.fillMaxSize()) {
//        // AR SceneView Components (adapt from sample)
//        val engine = rememberEngine()
//        val modelLoader = rememberModelLoader(engine)
//        val materialLoader = rememberMaterialLoader(engine)
//        val cameraNode = rememberARCameraNode(engine)
//        // Keep track of placed nodes
//        val childNodes = rememberNodes()
//        val view = rememberView(engine)
//        val collisionSystem = rememberCollisionSystem(view)
//
//        // Local mutable state for AR frame, needed for hit testing
//        var frame by remember { mutableStateOf<Frame?>(null) }
//
//        ARScene(
//            modifier = Modifier.fillMaxSize(),
//            childNodes = childNodes,
//            engine = engine,
//            view = view,
//            modelLoader = modelLoader,
//            collisionSystem = collisionSystem,
//            sessionConfiguration = { session, config ->
//                config.depthMode =
//                    when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
//                        true -> Config.DepthMode.AUTOMATIC
//                        else -> Config.DepthMode.DISABLED
//                    }
//                // Use HORIZONTAL_UPWARD_FACING for placing furniture on floors/tables
//                config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL
//                config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
//                config.instantPlacementMode =
//                    Config.InstantPlacementMode.DISABLED // Disable instant placement
//            },
//            cameraNode = cameraNode,
//            planeRenderer = state.showPlaneRenderer, // Control plane visibility from state
//            onTrackingFailureChanged = onTrackingFailureChanged, // Notify VM of tracking changes
//            onSessionUpdated = { session, updatedFrame ->
//                frame = updatedFrame // Update frame reference needed for hitTest
//            },
//            onGestureListener = rememberOnGestureListener( // Use SceneView's listener
//                onSingleTapConfirmed = { motionEvent, node ->
//                    // Only place if no node was tapped and an item is selected
//                    if (node == null && state.selectedItemModelFile != null) {
//                        frame?.hitTest(motionEvent.x, motionEvent.y)?.firstOrNull {
//                            // Check hit against detected planes
//                            it.trackable is Plane &&
//                                    (it.trackable as Plane).isPoseInPolygon(it.hitPose) &&
//                                    (it.trackable as Plane).type == Plane.Type.HORIZONTAL_UPWARD_FACING
//                        }?.createAnchorOrNull()?.let { anchor ->
//                            // Intent to hide planes after first placement
//                            onIntent(ArSceneIntent.HidePlanes)
//                            // Add the node for the SELECTED model
//                            childNodes += createFurnitureNode(
//                                engine = engine,
//                                modelLoader = modelLoader,
//                                materialLoader = materialLoader,
//                                modelFile = state.selectedItemModelFile, // Use selected model
//                                anchor = anchor
//                            )
//                            // Notify VM that item was placed to clear selection
//                            onIntent(ArSceneIntent.ItemPlaced)
//                        }
//                    }
//                    // TODO: Handle tapping existing nodes (e.g., for selection/deletion)
//                }
//                // Add listeners for long press (delete?), drag (move?), pinch (scale?) if needed
//            )
//        )
//
//        // Back Button (Top Left)
//        IconButton(
//            onClick = onNavigateBack,
//            modifier = Modifier
//                .align(Alignment.TopStart)
//                .systemBarsPadding() // Adjust for status bar
//                .padding(16.dp)
//        ) {
//            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
//        }
//
//
//        // Instruction Text (Top Center)
//        Text(
//            modifier = Modifier
//                .systemBarsPadding()
//                .fillMaxWidth()
//                .align(Alignment.TopCenter)
//                .padding(
//                    top = 16.dp,
//                    start = 72.dp,
//                    end = 72.dp
//                ), // Add padding to avoid button overlap
//            textAlign = TextAlign.Center,
//            fontSize = 16.sp, // Slightly smaller
//            color = Color.White,
//            text = state.instructionText // Use text from ViewModel state
//        )
//
//        // Collection Item Selector (Bottom)
//        Box(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .fillMaxWidth()
//                .systemBarsPadding() // Adjust for navigation bar
//                .background(Color.Black.copy(alpha = 0.5f))
//                .padding(vertical = 8.dp)
//        ) {
//            if (state.isLoadingCollection) {
//                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//            } else if (state.collectionError != null) {
//                Text(
//                    "Error: ${state.collectionError}",
//                    color = Color.Red,
//                    modifier = Modifier
//                        .align(Alignment.Center)
//                        .padding(8.dp)
//                )
//            } else if (state.availableItems.isEmpty()) {
//                Text(
//                    "Collection is empty.",
//                    color = Color.White,
//                    modifier = Modifier
//                        .align(Alignment.Center)
//                        .padding(8.dp)
//                )
//            } else {
//                LazyRow(
//                    contentPadding = PaddingValues(horizontal = 16.dp),
//                    horizontalArrangement = Arrangement.spacedBy(12.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    items(state.availableItems, key = { it.productId }) { item ->
//                        CollectionThumbnail(
//                            item = item,
//                            isSelected = state.selectedItemModelFile == item.modelFile,
//                            onClick = { onIntent(ArSceneIntent.SelectItemToPlace(item)) }
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun CollectionThumbnail(
//    item: CollectionItemUi,
//    isSelected: Boolean,
//    onClick: () -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .size(80.dp) // Size of the thumbnail
//            .clip(CircleShape)
//            .background(MaterialTheme.colorScheme.surfaceVariant)
//            .clickable(onClick = onClick)
//            .border( // Add border if selected
//                width = 3.dp,
//                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
//                shape = CircleShape
//            )
//            .padding(4.dp), // Inner padding
//        contentAlignment = Alignment.Center
//    ) {
//        AsyncImage(
//            model = ImageRequest.Builder(LocalContext.current)
//                .data(item.imageUrl)
//                .crossfade(true)
//                .build(),
//            contentDescription = item.name,
//            contentScale = ContentScale.Crop,
//            placeholder = painterResource(R.drawable.ic_launcher_background),
//            error = painterResource(R.drawable.ic_launcher_background),
//            modifier = Modifier
//                .fillMaxSize()
//                .clip(CircleShape)
//        )
//    }
//}
//
//
//// Adapted node creation function
//fun createFurnitureNode(
//    engine: Engine,
//    modelLoader: ModelLoader,
//    materialLoader: MaterialLoader,
//    modelFile: String, // Pass the specific model file path
//    anchor: Anchor
//): AnchorNode {
//    val anchorNode = AnchorNode(engine = engine, anchor = anchor)
//    val modelNode = ModelNode(
//        modelInstance = modelLoader.createModelInstance(assetFileLocation = modelFile), // Load from passed asset path
//        // Adjust scale as needed, maybe make it configurable per model?
//        scaleToUnits = 0.5f, // Example scale
//        centerOrigin = Position(0.0f) // Place origin at the bottom center
//    ).apply {
//        isEditable = true // Allow moving/scaling/rotating
//        // Configure editing constraints if desired
//        // editableScaleRange = 0.2f..2.0f
//    }
//    // Optional Bounding box for visual feedback during editing
//    val boundingBoxNode = CubeNode(
//        engine,
//        size = modelNode.extents,
//        center = modelNode.center,
//        materialInstance = materialLoader.createColorInstance(Color.White.copy(alpha = 0.5f))
//    ).apply {
//        isVisible = false // Only show when editing
//    }
//    modelNode.addChildNode(boundingBoxNode)
//    anchorNode.addChildNode(modelNode)
//
//    // Show/hide bounding box during editing
//    listOf(modelNode, anchorNode).forEach {
//        it.onEditingChanged = { editingTransforms ->
//            boundingBoxNode.isVisible = editingTransforms.isNotEmpty()
//        }
//    }
//    return anchorNode
//}
//
//
//// --- Previews (AR Previews are difficult, focus on UI parts) ---
//@Preview(showBackground = true)
//@Composable
//fun ArScreenUIPreview() {
//    TBCAcademyFinalTheme {
//        val sampleItems = listOf(
//            CollectionItemUi("p1", "Sofa", "url1", "models/sofa.glb"),
//            CollectionItemUi("p2", "Lamp", "url2", "models/lamp.glb")
//        )
//        ArScreenContent(
//            state = ArSceneState(
//                isLoadingCollection = false,
//                availableItems = sampleItems,
//                selectedItemModelFile = "models/sofa.glb",
//                instructionText = "Tap surface to place",
//                showPlaneRenderer = true
//            ),
//            onIntent = {},
//            onTrackingFailureChanged = {},
//            onNavigateBack = {}
//        )
//    }
//}
//
//@Preview
//@Composable
//fun CollectionThumbnailPreview() {
//    TBCAcademyFinalTheme {
//        CollectionThumbnail(
//            item = CollectionItemUi("p1", "Sofa", "", "m1"),
//            isSelected = false,
//            onClick = {}
//        )
//    }
//}
//
//@Preview
//@Composable
//fun CollectionThumbnailSelectedPreview() {
//    TBCAcademyFinalTheme {
//        CollectionThumbnail(
//            item = CollectionItemUi("p1", "Sofa", "", "m1"),
//            isSelected = true,
//            onClick = {}
//        )
//    }
//}