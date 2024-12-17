import {Image, StyleSheet, Platform, TouchableOpacity, Dimensions} from 'react-native';
import React, {useCallback, useContext} from "react";
import {ThemedText} from '@/components/ThemedText';
import {ThemedView} from '@/components/ThemedView';
import {useSafeAreaInsets} from "react-native-safe-area-context";
import Animated from "react-native-reanimated";
import SettingContext from "@/components/SettingComponentContext";

const {width, height} = Dimensions.get('window');


export default function HomeScreen() {


    const ctx = useContext(SettingContext);

    const handleClick = useCallback(() => {
        ctx.drawerActive();
    }, [ctx])


    const insets = useSafeAreaInsets();
    return (
        <React.Fragment>

            <Animated.View style={[styles.container]}>
                <ThemedView style={{paddingTop: insets.top * 2}}>
                    <ThemedText>Home</ThemedText>

                    <ThemedView style={{
                        alignItems: 'center',
                        justifyContent: 'center',
                        borderWidth: 1,
                    }}>
                        <TouchableOpacity onPress={handleClick}>
                            <ThemedText style={{
                                alignItems: 'center',
                                justifyContent: 'center'
                            }}>Click</ThemedText>
                        </TouchableOpacity>
                    </ThemedView>
                </ThemedView>
            </Animated.View>


        </React.Fragment>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1
    },


    titleContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        gap: 8,
    },
    stepContainer: {
        gap: 8,
        marginBottom: 8,
    },
    reactLogo: {
        height: 178,
        width: 290,
        bottom: 0,
        left: 0,
        position: 'absolute',
    },
    sideMenuStyle: {
        width: width * 0.75,
        margin: 0,
        zIndex: 1000,
    }
});
