import React, {forwardRef} from "react";
import {Dimensions, SafeAreaView, StyleSheet, View} from "react-native";
import {ThemedText} from "@/components/ThemedText";
import {ThemedView} from "@/components/ThemedView";


const DrawerComponent = forwardRef((props: any, ref: any) => {

    const drawerStyles = {
        drawer: { shadowColor: '#000000', shadowOpacity: 0.8, shadowRadius: 3},
        main: {paddingLeft: 3},
    }

    return (
        <View style={styles.safeAreaView}>
            <ThemedView style={styles.container}>
                <ThemedText style={styles.title}>Timeline</ThemedText>

                <ThemedView>
                    <ThemedView style={styles.swithBlock}>
                        <ThemedText style={styles.switchText}>
                            Ratings with reviews only
                        </ThemedText>
                    </ThemedView>
                    <ThemedText style={styles.description}>
                        When enabled, on your timeline we will only show ratings with reviews.
                    </ThemedText>
                </ThemedView>
            </ThemedView>
            <ThemedView>
                <ThemedText style={styles.link}>Press to call parent function</ThemedText>
            </ThemedView>
        </View>
    )

})

const {width, height} = Dimensions.get("window");

const styles = StyleSheet.create({
    safeAreaView: {
        flex: 1,
        backgroundColor: '#fff',
        zIndex: 999
    },
    container: {
        margin: 12,
        flex: 1,
    },
    title: {
        marginTop: 15,
        marginBottom: 10,
        color: '#444',
        fontSize: 14
    },
    swithBlock: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center'
    },
    switchText: {
        fontSize: 14,
        color: '#222'
    },
    link: {
        padding: 5,
        color: '#892853'
    },
    description: {
        fontSize: 13,
        color: '#555',
        marginTop: 12,
        marginBottom: 6
    }
});

export default DrawerComponent;