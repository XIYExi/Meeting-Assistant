import React from "react";
import {ThemedView} from "@/components/ThemedView";
import HeaderComponent from "@/components/HeaderComponent";
import {ThemedText} from "@/components/ThemedText";


interface ISettingProps{

}
function SettingApp() {

    return (
        <React.Fragment>
            <ThemedView style={{flex: 1}}>
                <ThemedText>hello</ThemedText>


                <ThemedText>hello</ThemedText>
                <ThemedText>hello</ThemedText>
                <ThemedText>hello</ThemedText>
                <ThemedText>hello</ThemedText>
                <ThemedText>hello</ThemedText>




            </ThemedView>
        </React.Fragment>
    )
}

export default SettingApp;