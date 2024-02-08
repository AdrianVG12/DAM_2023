<?php

/**
 * Script PHP para manejar la inserción de datos de Pokémon en la base de datos.
 * Recibe datos POST de nombre, tipo, habilidades e imagen_url de un Pokémon y los inserta en la base de datos.
 */

if(isset($_POST["name"]) && isset($_POST["type"]) && isset($_POST["abilities"]) && isset($_POST["image_url"])) {
    $pokemonName = $_POST["name"];
    $pokemonType = $_POST["type"];
    $pokemonAbilities = $_POST["abilities"];
    $pokemonImageUrl = $_POST["image_url"];

    // Conexión a la base de datos
    $servername = "localhost";
    $username = "root"; 
    $password = ""; 
    $dbname = "pokedex";

    $conexion = mysqli_connect($servername, $username, $password, $dbname);
    if (!$conexion) {
        echo "Error en la conexion a MySQL: " . mysqli_connect_error();
        exit();
    }

    // Prepara la consulta SQL para insertar los datos del Pokémon
    $sql = "INSERT INTO pokemon (name, type, abilities, image_url) VALUES ('$pokemonName', '$pokemonType', '$pokemonAbilities', '$pokemonImageUrl')";

    if (mysqli_query($conexion, $sql)) {
        echo "Registro insertado correctamente.";
    } else {
        echo "Error: " . $sql . "<br>" . mysqli_error($conexion);
    }

   
    mysqli_close($conexion);
} else {
    echo "Error: Se requieren todos los campos para guardar los datos del Pokémon.";
}
?>




