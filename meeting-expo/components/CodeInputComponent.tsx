import {
    StyleSheet,
    TextInput
} from "react-native";
import React from "react";
import {ThemedView} from "@/components/ThemedView";
import {ThemedText} from "@/components/ThemedText";

export default function CodeInputComponent({
                                      value,
                                      onValueChange,
                                      style,
                                      length = 4,
                                      spacing = 8,
                                  }: any) {

    const cells = value.split('').concat(Array(length - value.length).fill(''))

    return (
        <>
            <ThemedView style={[styles.container, style]} >
                <TextInput
                    style={styles.input}
                    autoFocus={true}
                    keyboardType="number-pad"
                    maxLength={length}
                    value={value}
                    clearButtonMode="never"
                    onChangeText={onValueChange}
                />
                <ThemedView style={[styles.cover, StyleSheet.absoluteFillObject]} pointerEvents="none">
                    {cells.map((value: any, index: number) => (
                        <ThemedView
                            style={[
                                styles.cell,
                                { width: 48, height: 48, marginLeft: index === 0 ? 0 : spacing },
                            ]}
                            key={index}>
                            <ThemedText style={styles.text}>{value}</ThemedText>
                        </ThemedView>
                    ))}
                </ThemedView>
            </ThemedView>
        </>
    )
}

const styles = StyleSheet.create({
    container: {
        height: 44,
    },
    input: {
        flex: 1,
        padding: 0,
        margin: 0,
        opacity: 0,
    },
    cover: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'center',
    },
    cell: {
        borderRadius: 8,
        backgroundColor: '#ECECEC',
        justifyContent: 'center',
        alignItems: 'center',
    },
    text: {
        color: '#333333',
        fontSize: 17,
        fontWeight: 'bold',
    },
})
