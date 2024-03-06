<template>
    <div id="map" style="height: 500px;"></div>
</template>

<script>


export default {
  name: 'GPS',
  mounted() {
    var map = L.map('map').setView([37.77, -122.447], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '© OpenStreetMap contributors'
    }).addTo(map);

    L.Routing.control({
        waypoints: [
            L.latLng(37.77, -122.447),  // 起点
            L.latLng(37.768, -122.511)  // 终点
        ],
        routeWhileDragging: true
    }).addTo(map);
  }
};
</script>