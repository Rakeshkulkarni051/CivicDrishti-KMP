package com.rvitmca64.civicdrishti.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import java.awt.Desktop
import java.net.InetSocketAddress
import com.sun.net.httpserver.HttpServer



@Composable
fun HeatMapScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // 🔥 MAP SECTION (takes all remaining space)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
           MapWebView()
        }

        // 🔻 SPACE FOR BOTTOM NAV BAR
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp) // adjust based on your nav bar height
        )
    }
}
@Composable
fun MapWebView() {
    LaunchedEffect(Unit) {

        val html = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC-0SOhv7PQfKYPtv-Mhp7SEV9IgLZgdaI&libraries=visualization"></script>

                <script>
                    function initMap() {
                        var center = {lat: 12.9716, lng: 77.5946};

                        var map = new google.maps.Map(document.getElementById('map'), {
                            zoom: 13,
                            center: center
                        });

                      var heatmapData = [
    {location: new google.maps.LatLng(12.9063, 77.5857), weight: 10},
    {location: new google.maps.LatLng(12.9070, 77.5865), weight: 8},
    {location: new google.maps.LatLng(12.9055, 77.5840), weight: 6},
    {location: new google.maps.LatLng(12.8947, 77.5994), weight: 8},
    {location: new google.maps.LatLng(12.8788, 77.5636), weight: 9},
    {location: new google.maps.LatLng(12.8747, 77.5722), weight: 8},
    {location: new google.maps.LatLng(12.8719, 77.5808), weight: 6}
];


                        var heatmap = new google.maps.visualization.HeatmapLayer({
    data: heatmapData,
    radius: 40,
    opacity: 0.7
});

heatmap.set('gradient', [
    'rgba(0, 255, 255, 0)',
    'rgba(0, 255, 255, 1)',
    'rgba(0, 191, 255, 1)',
    'rgba(0, 127, 255, 1)',
    'rgba(0, 63, 255, 1)',
    'rgba(0, 0, 255, 1)',
    'rgba(0, 0, 223, 1)',
    'rgba(0, 0, 191, 1)',
    'rgba(0, 0, 159, 1)',
    'rgba(0, 0, 127, 1)',
    'rgba(63, 0, 91, 1)',
    'rgba(127, 0, 63, 1)',
    'rgba(191, 0, 31, 1)',
    'rgba(255, 0, 0, 1)'
]);

                        heatmap.setMap(map);
                    }
                </script>
            </head>

            <body onload="initMap()" style="margin:0">
                <div id="map" style="width:100vw; height:100vh;"></div>
            </body>
            </html>
        """.trimIndent()

        // 🔥 Start local server
        val server = HttpServer.create(InetSocketAddress(8080), 0)

        server.createContext("/") { exchange ->
            val response = html.toByteArray()
            exchange.sendResponseHeaders(200, response.size.toLong())
            exchange.responseBody.use { it.write(response) }
        }

        server.start()

        // 🔥 Open in browser (SAFE HTTP)
        Desktop.getDesktop().browse(java.net.URI("http://localhost:8080"))
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Launching Heatmap Dashboard...")
    }
}