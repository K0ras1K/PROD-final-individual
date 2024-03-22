import osmnx as ox
import networkx as nx
import folium

#56.320243, 44.030647
#56.297074, 44.034753

target_place_1 = "Москва, Россия"
target_place_2 = "Нижний Новгород, Россия"

# Define start and end coordinates
start_latlng = (55.738983, 37.598226)
end_latlng = (56.297074, 44.034753)

# Get the nearest place to the coordinates
place_gdf = ox.geocode_to_gdf('{},{}'.format(start_latlng[0], start_latlng[1]))
place_gdf_1 = ox.geocode_to_gdf('{},{}'.format(end_latlng[0], end_latlng[1]))

# Print the name of the nearest place
print(place_gdf.iloc[0]['display_name'])  # Output: Moscow, Central Federal District, Russia
print(place_gdf_1.iloc[0]['display_name'])  # Output: Moscow, Central Federal District, Russia

mode = 'drive'
graph1 = ox.graph_from_place(place_gdf.iloc[0]['display_name'], network_type=mode)
graph2 = ox.graph_from_place(place_gdf_1.iloc[0]['display_name'], network_type=mode)
orig_node = ox.distance.nearest_nodes(graph1, start_latlng[1], start_latlng[0])
dest_node = ox.distance.nearest_nodes(graph2, end_latlng[1], end_latlng[0])

# Calculate the shortest route
optimizer = 'length'
try:
    shortest_route = nx.shortest_path(nx.compose(graph1, graph2), orig_node, dest_node, weight=optimizer)
except nx.NetworkXNoPath:
    print("No path found between the start and end nodes. Plotting a straight line instead.")
    shortest_route = [orig_node, dest_node]

# Plot the route on a Folium map
shortest_route_map = folium.Map(location=[(start_latlng[0] + end_latlng[0]) / 2, (start_latlng[1] + end_latlng[1]) / 2], zoom_start=4)

if len(shortest_route) > 2:
    # Plot the route as a line
    route_line = folium.PolyLine(locations=[(node[0], node[1]) for node in shortest_route], color='blue', weight=3)
    route_line.add_to(shortest_route_map)
else:
    # Plot a straight line between the start and end points
    route_line = folium.PolyLine(locations=[start_latlng, end_latlng], color='blue', weight=3)
    route_line.add_to(shortest_route_map)

# Add start and end markers
start_marker = folium.Marker(location=start_latlng, popup='Start', icon=folium.Icon(color='green'))
end_marker = folium.Marker(location=end_latlng, popup='End', icon=folium.Icon(color='red'))
start_marker.add_to(shortest_route_map)
end_marker.add_to(shortest_route_map)

# Save the map as a PNG file
shortest_route_map.save('shortest_route.html')