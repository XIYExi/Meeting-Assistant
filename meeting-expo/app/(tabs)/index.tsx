import {Image, StyleSheet, Platform, TouchableOpacity, Dimensions, View, Pressable, FlatList} from 'react-native';
import React, {useCallback, useContext} from "react";
import {ThemedText} from '@/components/ThemedText';
import {ThemedView} from '@/components/ThemedView';
import {useSafeAreaInsets} from "react-native-safe-area-context";
import Animated from "react-native-reanimated";
import SettingContext from "@/components/SettingComponentContext";
import PageComponent from "@/components/PageComponent";
import SettingImage from '@/assets/components/tabs/setting_icon.png';
import AgentImage from '@/assets/components/tabs/agent.png';
import {HelloWave} from "@/components/HelloWave";
import ParallaxScrollView from "@/components/ParallaxScrollView";
import {Collapsible} from "@/components/Collapsible";
import {ExternalLink} from "@/components/ExternalLink";
import EventsCardComponent from "@/components/EventsCardComponent";

const {width, height} = Dimensions.get('window');


export default function HomeScreen() {

    const ctx = useContext(SettingContext);
    const handleClick = useCallback(() => {
        ctx.drawerActive();
    }, [ctx])
    const insets = useSafeAreaInsets();


    return (
        <React.Fragment>
            <ParallaxScrollView
                style={{padding: 0}}
                headerImage={
                <ThemedView style={{paddingTop: insets.top * 2}}>
                    <View style={[styles.header]}>
                        <View style={styles.introWrapper}>
                            <ThemedText style={styles.intro}>会议助手</ThemedText>
                            <View style={styles.introWaveWrapper}>
                                <ThemedText style={styles.introAppName}>AI Agent</ThemedText>
                                <HelloWave />
                            </View>
                        </View>

                        <Pressable style={styles.settingWrapper} onPress={handleClick}>
                            <Image source={SettingImage} style={styles.settingImage} />
                        </Pressable>
                    </View>
                    <PageComponent>
                        <View style={styles.agentWrapper}>
                            <View style={styles.agent}>
                                <Image source={AgentImage} style={styles.agentImage}/>
                            </View>
                        </View>

                        <TouchableOpacity style={styles.chatButtonConfirmWrapper}>
                            <View style={styles.chatButtonWrapper}>
                                <ThemedText style={styles.chatText}>对话</ThemedText>
                            </View>
                        </TouchableOpacity>
                    </PageComponent>
                </ThemedView>
                }
                headerBackgroundColor={{ light: '#D0D0D0', dark: '#353636' }}
            >
                <View style={styles.restWrapper}>
                    <View style={styles.events}>

                        <EventsCardComponent />
                    </View>

                    <View style={styles.notices}>

                    </View>
                </View>
            </ParallaxScrollView>

        </React.Fragment>
    );
}

const styles = StyleSheet.create({
    restWrapper: {
        backgroundColor: '#fcfcfc',
        width: '100%',
        minHeight: 1000,
    },
    events: {

    },
    notices: {

    },


    container: {
        flex: 1
    },
    header: {
        height: 80,
        alignItems: 'flex-end',
        justifyContent: 'space-between',
        paddingHorizontal: 20,
        flexDirection: 'row',
        // borderWidth: 1,
    },
    agentWrapper: {
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: 45,
    },
    agent: {
        width: 210,
        height: 210,
        // borderWidth: 1,
        borderRadius: '50%',
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#f76b10',
    },
    agentImage: {
        width: 130,
        height: 130
    },
    introWrapper: {
        marginTop: 30
        //borderWidth: 1,
    },
    intro: {
        width: '100%',
        paddingTop: 5,
        fontWeight: '600',
        fontSize: 32,
        lineHeight: 30,
    },
    introAppName: {
        fontWeight: '700',
        fontSize: 32,
        lineHeight: 32,
        textAlign: 'center',
        color: '#f76b10',
    },
    introWaveWrapper: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'flex-start',
    },
    chatButtonConfirmWrapper: {
        justifyContent: 'center',
        alignItems: 'center',
    },
    chatButtonWrapper: {
        width: '65%',
        height: 45,
        // borderWidth: 1,
        borderRadius: 40,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#f76b10',
        marginVertical: 30
    },
    chatText: {
        textAlign: 'center',
        color: 'white',
    },


    settingWrapper: {
        width: 50,
        height: '100%',
        justifyContent: 'center',
        alignItems: 'center',
        // borderWidth: 1,
    },
    settingImage: {
        width: 32,
        height: 32,
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
