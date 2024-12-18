import React from "react";
import {ThemedView} from "@/components/ThemedView";
import {Dimensions, Image, Pressable, StyleSheet, Text, TouchableOpacity, View} from "react-native";
import {ThemedText} from "@/components/ThemedText";
//@ts-ignore
import EventPreviewImage from '@/assets/components/tabs/events-preview.png';
//@ts-ignore
import CalendarIconImage from '@/assets/components/tabs/calendar-icon.png';
//@ts-ignore
import LocationIconImage from '@/assets/components/tabs/location-icon.png';
//@ts-ignore
import LikeIconImage from '@/assets/components/tabs/like-icon.png';
//@ts-ignore
import CollectionIconImage from '@/assets/components/tabs/collection-icon.png';

const {width, height} = Dimensions.get("window");

/**
 @name:活动卡片
 @description:主页面使用，类似大麦的预览，但是是按照时间顺序排列;只推送主会场
 @author: xiye.Cayon
 @time: 2024-12-18 20:28:18
 */
interface IEventsCardComponentProps {

}
function EventsCardComponent(props: IEventsCardComponentProps) {

    return (
        <React.Fragment>
            <ThemedView style={styles.cardContainer}>
                <View style={styles.imageWrapper}>
                    <Image source={EventPreviewImage} style={styles.image} />
                </View>

                <View style={styles.msgWrapper}>
                    <ThemedText style={styles.msgTitle}>西湖论剑大会</ThemedText>
                    <View style={styles.metaWrapper}>
                        <View style={styles.metaIconWrapper}>
                            <Image source={CalendarIconImage} style={styles.iconImage} />
                            <ThemedText style={styles.meta}>12月18~19号</ThemedText>
                        </View>

                        <View style={styles.metaIconWrapper}>
                            <Image source={LocationIconImage} style={styles.iconImage} />
                            <ThemedText style={styles.meta}>中国,杭州</ThemedText>
                        </View>
                    </View>

                    <View style={styles.detailsButtonWrapper}>

                        <View style={styles.footerIconWrapper}>
                            <Pressable style={[styles.likeIconWrapper, {marginRight: 14}]}>
                                <Image source={LikeIconImage} style={styles.likeIcon}/>
                            </Pressable>
                            <Pressable style={styles.likeIconWrapper}>
                                <Image source={CollectionIconImage} style={styles.likeIcon}/>
                            </Pressable>

                        </View>

                        <TouchableOpacity style={styles.detailsWrapper}>
                            <Text style={styles.detailsButton}>Details</Text>
                        </TouchableOpacity>
                    </View>

                </View>
            </ThemedView>
        </React.Fragment>
    )
}

const styles = StyleSheet.create({
    footerIconWrapper: {
        flexDirection: 'row',
        paddingVertical: 3,
        paddingHorizontal: 15,
        borderRadius: 12,
        backgroundColor: '#e8f0f3',
    },
    likeIconWrapper: {
        width: 32,
        height: 32,
        justifyContent: 'center',
        alignItems: 'center',
        borderRadius: 50,
        backgroundColor: '#f2f2f6',
        opacity: 0.9,
    },
    likeIcon: {
        width: 19,
        height: 19,
    },
    metaIconWrapper: {
      flexDirection: 'row',
      justifyContent: 'center',
        alignItems: 'center',

    },
    iconImage: {
        width: 12,
        height: 12,
        marginRight: 3,
    },
    detailsButtonWrapper: {
        marginTop: 6,
        alignItems: 'center',
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    detailsWrapper: {
        width: 80,
        height: 35,
        borderRadius: 10,
        backgroundColor: '#2c2c2c',
        alignItems: 'center',
        justifyContent: 'center',
    },
    detailsButton: {
        textAlign: 'center',
        color: 'white',
        fontWeight: '500'
    },
    msgTitle: {
        marginTop: 5,
        fontSize: 19,
        fontWeight: '700'
    },
    metaWrapper: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        width: '100%',
        marginTop: 8,
    },
    meta: {
        fontSize: 15,
        fontWeight: '300',
        color: 'gray'
    },
    imageWrapper: {
        width: '100%',
        height: '55%',
    },
    image: {
        width: '100%',
        height: '100%',
        borderRadius: 20,
        resizeMode: 'contain'
    },
    msgWrapper: {
        width: width * 0.55,
        height: '45%',
        // borderWidth: 1,
        paddingHorizontal: 5
    },
    cardContainer: {
        paddingVertical: 5,
        paddingHorizontal: 8,
        borderRadius: 20,
        // borderWidth: 1,
        width: width * 0.6,
        height: width * 0.65,
        marginLeft: 20,
    },
});

export default EventsCardComponent;