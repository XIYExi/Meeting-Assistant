import React from "react";
import {StyleProp, StyleSheet, useColorScheme, ViewStyle} from "react-native";
import {ThemedText} from "@/components/ThemedText";
import {ThemedView} from "@/components/ThemedView";

/**
 @name: button组件
 @description:
 @author: xiye.Cayon
 @time: 2024-12-15 20:35:09
 */
interface IButtonProps {
    text: string;
    style?: StyleProp<ViewStyle>;
}

function ButtonComponent(props: IButtonProps) {

    const colorScheme = useColorScheme();

    return (
        <React.Fragment>
            <ThemedView style={[styles.button, {
                borderColor: colorScheme === 'dark' ? 'white' : 'black'
            }, props.style]}>
                <ThemedText style={{
                    fontWeight: '600',
                    textAlign: 'center',
                }}>
                    {props.text}
                </ThemedText>
            </ThemedView>
        </React.Fragment>
    )
}


const styles = StyleSheet.create({
    button: {
        borderWidth: 1,
        paddingVertical: 10,
        borderRadius: 16,
    },
});

export default ButtonComponent;
