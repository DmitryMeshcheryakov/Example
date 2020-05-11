package com.android.example.core.ui.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.android.example.R
import com.android.example.core.model.BaseMapMarker
import com.android.example.core.utils.DayNightUtils
import com.android.example.features.mvp.BaseMvpFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import kotlin.math.max

abstract class BaseMapFragment<M : BaseMapMarker>(@LayoutRes layoutRes: Int) :
    BaseMvpFragment(layoutRes), OnMapReadyCallback {

    protected open val markerSize: Float = 48f //dp

    protected open val getClusterIconResId: Int? = R.drawable.bg_map_cluster

    protected open val getClusterTextAppearanceResId: Int? = R.style.BaseClusterTextAppearance

    protected open var showMyPosition: Boolean = false
        set(value) {
            field = value
            map?.isMyLocationEnabled = value
        }

    protected open var showCompass: Boolean = false
        set(value) {
            field = value
            map?.uiSettings?.isCompassEnabled = value
        }

    protected open var showZoomButton: Boolean = false
        set(value) {
            field = value
            map?.uiSettings?.isZoomControlsEnabled = value
        }

    protected open var showMyPositionButton: Boolean = false
        set(value) {
            field = value
            map?.uiSettings?.isMyLocationButtonEnabled = value
        }

    protected open var useClusters: Boolean = false

    protected var map: GoogleMap? = null

    protected var clusterManager: ClusterManager<M>? = null

    protected abstract fun getMapView(): MapView?

    protected abstract fun getMarkerClickListener(): MarkerClickListener?

    protected open fun getDefaultBounds(): LatLngBounds {
        return LatLngBounds.builder()
            .include(LatLng(52.284099, 23.178592))
            .include(LatLng(51.414125, 28.749118))
            .include(LatLng(53.388236, 32.774547))
            .include(LatLng(56.177344, 28.164910))
            .build()
    }

    protected open fun moveToPosition(position: LatLng, callback: MoveCallback? = null) {
        val maxZoom = map?.maxZoomLevel ?: 20f
        val minZoom = map?.minZoomLevel ?: 0f
        val diff = (maxZoom - minZoom) / 100f
        val zoom = max(map?.cameraPosition?.zoom ?: (90 * diff), 90 * diff)

        map?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(position, zoom),
            object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    callback?.onMoveToPosition()
                }

                override fun onCancel() {
                    callback?.onMoveToPosition()
                }
            })
    }

    protected open fun moveToBounds(bounds: LatLngBounds, callback: MoveCallback? = null) {
        map?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0),
            object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    callback?.onMoveToBounds()
                }

                override fun onCancel() {
                    callback?.onMoveToBounds()
                }
            })
    }

    protected open fun zoomIn(callback: ZoomCallback? = null) {
        map?.animateCamera(CameraUpdateFactory.zoomIn(), object : GoogleMap.CancelableCallback {
            override fun onFinish() {
                callback?.onZoomIn()
            }

            override fun onCancel() {
                callback?.onZoomIn()
            }
        })
    }

    protected open fun zoomOut(callback: ZoomCallback? = null) {
        map?.animateCamera(CameraUpdateFactory.zoomOut(), object : GoogleMap.CancelableCallback {
            override fun onFinish() {
                callback?.onZoomOut()
            }

            override fun onCancel() {
                callback?.onZoomOut()
            }
        })
    }

    protected open fun zoomTo(value: Float, callback: ZoomCallback? = null) {
        map?.animateCamera(
            CameraUpdateFactory.zoomTo(value),
            object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    callback?.onZoom(value)
                }

                override fun onCancel() {
                    callback?.onZoom(value)
                }
            })
    }

    protected open fun clearMarkers() {
        when (useClusters) {
            true -> clusterManager?.clearItems()
            false -> map?.clear()
        }
    }

    protected open fun addMarkers(markers: List<M>?) {
        markers?.let { mapMarkers ->
            when (useClusters) {
                true -> {
                    clusterManager?.addItems(mapMarkers)
                    clusterManager?.cluster()
                }
                false -> {
                    mapMarkers.forEach {
                        map?.addMarker(
                            MarkerOptions()
                                .title(it.title)
                                .snippet(it.id)
                                .position(it.position)
                                .icon(createMapIcon(it.imageRes))
                        )
                    }
                }
            }
        }
    }

    protected open fun addMarker(marker: M?, moveCamera: Boolean = false) {
        marker?.let { mapMarker ->
            when (useClusters) {
                true -> {
                    clusterManager?.addItem(mapMarker)
                    clusterManager?.cluster()
                }
                false -> {
                    map?.addMarker(
                        MarkerOptions()
                            .position(mapMarker.position)
                            .icon(BitmapDescriptorFactory.fromResource(mapMarker.imageRes))
                    )
                }
            }

            if (moveCamera) moveToPosition(marker.position)
        }
    }

    protected fun isFirstCreate(): Boolean {
        val flag = !(arguments?.getBoolean(IS_FIRST_CREATE_FLAG, false) ?: false)
        arguments?.putBoolean(IS_FIRST_CREATE_FLAG, true)
        return flag
    }

    protected open fun onMapLoaded() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMapView()?.onCreate(savedInstanceState)
        getMapView()?.getMapAsync(this)
    }


    override fun onMapReady(p0: GoogleMap?) {
        map = p0
        map?.isMyLocationEnabled = showMyPosition
        map?.uiSettings?.setAllGesturesEnabled(true)
        map?.uiSettings?.isCompassEnabled = showCompass
        map?.uiSettings?.isZoomControlsEnabled = showZoomButton
        map?.uiSettings?.isMyLocationButtonEnabled = showMyPositionButton
        map?.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                context,
                if (DayNightUtils.isDarkModeEnabled(context)) R.raw.map_style_dark
                else R.raw.map_style_light
            )
        )

        if (useClusters) {
            context?.let { context ->
                clusterManager = ClusterManager(context, p0)
                if (p0 != null && clusterManager != null) {
                    clusterManager?.renderer = ClusterRenderer(context, p0, clusterManager)
                    clusterManager?.algorithm = NonHierarchicalDistanceBasedAlgorithm<M>()
                    clusterManager?.setAnimation(true)
                    clusterManager?.setOnClusterItemClickListener(ClusterManager.OnClusterItemClickListener {
                        return@OnClusterItemClickListener getMarkerClickListener()?.onMarkerClick(it.id)
                            ?: false
                    })
                    p0.setOnCameraIdleListener(clusterManager)
                    p0.setOnMarkerClickListener(clusterManager)
                    p0.setOnInfoWindowClickListener(clusterManager)
                }
            }
        } else map?.setOnMarkerClickListener { marker ->
            getMarkerClickListener()?.onMarkerClick(
                marker.snippet
            ) ?: false
        }

        map?.setOnMapLoadedCallback { onMapLoaded() }
    }

    override fun onResume() {
        getMapView()?.onResume()
        super.onResume()
    }

    override fun onPause() {
        getMapView()?.onPause()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        getMapView()?.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        getMapView()?.onLowMemory()
        super.onLowMemory()
    }

    override fun onDestroy() {
        getMapView()?.onDestroy()
        super.onDestroy()
    }

    protected fun createMapIcon(@DrawableRes resId: Int): BitmapDescriptor {
        val icon = BitmapFactory.decodeResource(resources, resId)

        val size = markerSize * resources.displayMetrics.density
        val diff = size / max(icon.height, icon.width)
        val newHeight = (icon.height * diff).toInt()
        val newWidth = (icon.width * diff).toInt()

        val scaledIcon = Bitmap.createScaledBitmap(
            icon,
            newWidth,
            newHeight,
            false
        )
        return BitmapDescriptorFactory.fromBitmap(scaledIcon)
    }

    inner class ClusterRenderer(
        private val context: Context?,
        map: GoogleMap?,
        clusterManager: ClusterManager<M>?
    ) :
        DefaultClusterRenderer<M>(context, map, clusterManager) {

        private val clusterIconGenerator: IconGenerator = IconGenerator(context)

        override fun onBeforeClusterItemRendered(item: M?, markerOptions: MarkerOptions?) {
            val markerDescriptor = createMapIcon(item?.imageRes!!)
            markerOptions?.icon(markerDescriptor)?.snippet(item.title)
        }

        override fun onBeforeClusterRendered(cluster: Cluster<M>, markerOptions: MarkerOptions) {
            context?.let {
                clusterIconGenerator.setBackground(
                    ContextCompat.getDrawable(
                        context,
                        getClusterIconResId!!
                    )
                )
                clusterIconGenerator.setTextAppearance(getClusterTextAppearanceResId!!)
                val icon = clusterIconGenerator.makeIcon(cluster.size.toString())
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
            }
        }
    }

    interface MarkerClickListener {

        fun onMarkerClick(id: String): Boolean = false
    }

    interface MoveCallback {

        fun onMoveToPosition() {}

        fun onMoveToBounds() {}
    }

    interface ZoomCallback {

        fun onZoom(value: Float) {}

        fun onZoomIn() {}

        fun onZoomOut() {}
    }

    companion object {
        private const val IS_FIRST_CREATE_FLAG = "IS_FIRST_CREATE_FLAG"
    }
}