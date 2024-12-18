import React, {forwardRef, useContext} from "react";
import {Button, Dimensions, SafeAreaView, StyleSheet, View} from "react-native";
import {ThemedText} from "@/components/ThemedText";
import {ThemedView} from "@/components/ThemedView";
import {useColorScheme} from "@/hooks/useColorScheme";
import {useNavigation} from "@react-navigation/native";
import SettingContext from "@/components/SettingComponentContext";


const DrawerComponent = forwardRef((props: any, ref: any) => {

    const colorSchema = useColorScheme();
    
    const ctx = useContext(SettingContext);
    const navigation = useNavigation();
    const handleGotoExplore = () => {
        ctx.drawerClose();
        // @ts-ignore
        navigation.navigate('user')
    }

    return (
        <ThemedView style={[styles.container, {
            backgroundColor: colorSchema === 'dark' ? '#212121' : '#fff'
        }]}>
            <View style={styles.contentContainer}>
                <View style={styles.containerProfile}>
                    <ThemedText type='title' style={styles.system}>System</ThemedText>
                </View>
            </View>

            <View style={{maxWidth: 100, marginBottom: 20}}>
                <Button title={"hello"} onPress={handleGotoExplore}/>
            </View>
            <View style={{maxWidth: 100, marginBottom: 20}}>
                <Button title={"hello"} />
            </View>
            <View style={{maxWidth: 100, marginBottom: 20}}>
                <Button title={"hello"} />
            </View>
            <View style={{maxWidth: 100, marginBottom: 20}}>
                <Button title={"hello"} />
            </View>
        </ThemedView>
    )

})

const {width, height} = Dimensions.get("window");

const styles = StyleSheet.create({
    container: {
        ...StyleSheet.absoluteFillObject,
        zIndex: -99,
        borderWidth: 1,
    },
    contentContainer: {
        paddingTop: 120,
        marginHorizontal: 30,
        maxWidth: 180,
    },
    containerProfile: {
        gap: 14,
        height: 60
    },
    system: {
        marginTop: 10,
        fontWeight: "500"
    }
});

export default DrawerComponent;