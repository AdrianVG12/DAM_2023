<?php

/**
 * Script PHP para manejar la recuperación de datos de Pokémon desde la base de datos y devolverlos en formato JSON.
 */

// Configuración de la conexión a la base de datos
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "pokedex";


$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Error de conexión: " . $conn->connect_error);
}

$sql = "SELECT name, type FROM pokemon";
$result = $conn->query($sql);
$pokemonList = array();

if ($result->num_rows > 0) {
    // Iterar sobre los resultados y almacenarlos en un array
    while ($row = $result->fetch_assoc()) {
        $pokemon = array(
            "name" => $row["name"],
            "type" => $row["type"]
        );
        array_push($pokemonList, $pokemon);
    }
} else {
    echo "No se encontraron Pokémon guardados.";
}

echo json_encode($pokemonList);


$conn->close();
?>

