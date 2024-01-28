// App.js
import React from 'react';
import { StyleSheet, View } from 'react-native';
import CalculadoraIMC from './components/CalculadoraIMC';

export default function App() {
  return (
    <View style={styles.container}>
      <CalculadoraIMC />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'purple',
  },
});

