import React from "react";
import {Pressable, StyleSheet, Text, View} from 'react-native';
import Animated, {SharedValue, useAnimatedStyle} from "react-native-reanimated";

/**
 @name:侧边DrawerComponent配合使用得遮罩
 @description:
 @author: xiye.Cayon
 @time: 2024-12-17 14:39:00
 */
interface IOverlayComponentProps {
    active: SharedValue<boolean>;
}
const OverlayComponent = (props: IOverlayComponentProps) => {
    const {active} = props;
    const animatedStyle = useAnimatedStyle(() => {
        return {
            display: active.value ? 'flex' : 'none',
        };
    });
    return (
        <Animated.View style={[styles.container, animatedStyle]}>
            <Pressable style={styles.container} onPress={() => {
                active.value = false;
            }} />
        </Animated.View>
    )
}

const styles = StyleSheet.create({
    container: {
        ...StyleSheet.absoluteFillObject,
    }
});

export default OverlayComponent;