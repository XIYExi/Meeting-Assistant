import React from "react";
import {ThemedView} from "@/components/ThemedView";
import {StyleSheet, View} from "react-native";

interface IEventsCardComponentProps {

}

function EventsCardComponent(props: IEventsCardComponentProps) {

    return (
        <React.Fragment>
            <View style={styles.cardContainer}>

            </View>
        </React.Fragment>
    )
}

const styles = StyleSheet.create({
    cardContainer: {
        padding: 5,
        borderRadius: 10,
        borderWidth: 1,

    },
});

export default EventsCardComponent;