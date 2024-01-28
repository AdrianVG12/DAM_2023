// components/CalculadoraIMC.js
import React, { useState } from 'react';
import { View, Text, TextInput, Button, StyleSheet } from 'react-native';

const CalculadoraIMC = () => {
  const [peso, setPeso] = useState('');
  const [altura, setAltura] = useState('');
  const [resultado, setResultado] = useState('');
  const [colorResultado, setColorResultado] = useState('');

  const calcularIMC = () => {
    const pesoNum = parseFloat(peso);
    const alturaNum = parseFloat(altura) / 100; // Convertir altura a metros
    const imc = pesoNum / (alturaNum * alturaNum);

    let clasificacion = '';

    if (imc < 18.5) {
      clasificacion = 'Peso insuficiente';
      setColorResultado('green');
    } else if (imc >= 18.5 && imc < 25) {
      clasificacion = 'Normopeso';
      setColorResultado('green');
    } else if (imc >= 25 && imc < 27) {
      clasificacion = 'Sobrepeso grado I';
      setColorResultado('orange');
    } else if (imc >= 27 && imc < 30) {
      clasificacion = 'Sobrepeso grado II (preobesidad)';
      setColorResultado('orange');
    } else if (imc >= 30 && imc < 35) {
      clasificacion = 'Obesidad de tipo I';
      setColorResultado('red');
    } else if (imc >= 35 && imc < 40) {
      clasificacion = 'Obesidad de tipo II';
      setColorResultado('red');
    } else if (imc >= 40 && imc < 50) {
      clasificacion = 'Obesidad de tipo III (mórbida)';
      setColorResultado('red');
    } else {
      clasificacion = 'Obesidad de tipo IV (extrema)';
      setColorResultado('red');
    }

    setResultado(`${clasificacion}`);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.titulo}>CALCULADORA IMC</Text>
      <View style={styles.calculadora}>
        <View style={styles.seccionDatos}>
          <Text style={styles.subtitulo}>Datos</Text>
          <Text style={styles.etiqueta}>PESO</Text>
          <TextInput
            style={styles.input}
            placeholder="Ingrese su peso"
            keyboardType="numeric"
            value={peso}
            onChangeText={(text) => setPeso(text)}
          />
          <Text style={styles.etiqueta}>ALTURA</Text>
          <TextInput
            style={styles.input}
            placeholder="Ingrese su altura"
            keyboardType="numeric"
            value={altura}
            onChangeText={(text) => setAltura(text)}
          />
          <Button title="Calcular IMC" onPress={calcularIMC} color="#007BFF" />
        </View>
        <Text style={styles.ResultadoText}>Resultado:</Text>
        <Text style={{ color: colorResultado}}>
          {resultado}
        </Text>
      </View>
      <Text style={styles.pieDeTexto}>AEV: Adrián Vicente García</Text>
      
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'purple',
  },
  titulo: {
    color: 'red',
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  calculadora: {
    backgroundColor: 'white',
    padding: 20,
    borderRadius: 5,
    width: '200%', 
  },
  subtitulo: {
    color: 'red',
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 10,
  },
  seccionDatos: {
    marginBottom: 20,
  },
  etiqueta: {
    color: 'blue',
    marginBottom: 5,
  },
  input: {
    borderWidth: 1,
    borderColor: 'lightgrey',
    padding: 8,
    marginBottom: 10,
  },
  pieDeTexto: {
    position: 'absolute',
    bottom: 170, // Ajusta la posición vertical según tus necesidades
    fontSize: 13,
    color: 'white',
  }
});

export default CalculadoraIMC;



