import React from "react";
import {ThemedView} from "@/components/ThemedView";
import {ThemedText} from "@/components/ThemedText";
import {View, StyleSheet, Image, Text, TouchableOpacity} from "react-native";
// @ts-ignore
import RecPreviewImage from '@/assets/components/tabs/rec-preview.png';

/**
 @name:推荐活动小卡片页面
 @description:总览下面的活动推荐，推荐算法推荐，可以推送分会场
 @author: xiye.Cayon
 @time: 2024-12-18 20:29:30
 */
interface IRecommendationCardComponentProps {}
function RecommendationCardComponent(props: IRecommendationCardComponentProps) {
    return (
        <React.Fragment>
            <ThemedView style={styles.container}>
                <View style={styles.imageWrapper}>
                    <Image source={RecPreviewImage} style={styles.image} />
                </View>

                <View style={styles.msgWrapper}>
                    <ThemedText style={styles.msgTitle}>开发者大会2024</ThemedText>
                    <View style={styles.metaWrapper}>
                        <ThemedText style={styles.meta}>12月29号~30号</ThemedText>
                        <ThemedText style={styles.meta}>中国,无锡</ThemedText>
                    </View>
                </View>

                <View style={styles.detailsWrapper}>
                    <TouchableOpacity style={styles.detailsButton}>
                        <Text style={styles.details}>Details</Text>
                    </TouchableOpacity>
                </View>
            </ThemedView>
        </React.Fragment>
    )
}

const styles = StyleSheet.create({
    container: {
        flexDirection: 'row',
        width: '100%',
        height: 72,
        borderRadius: 20,
        // borderWidth: 1,
        paddingVertical: 4,
        paddingHorizontal: 6,
        position: 'relative',
        marginVertical: 6,
    },
    imageWrapper: {
        width: 64,
        height: 64,
        justifyContent: 'center',
        alignItems: 'center'
    },
    image: {
        width: 58,
        height: 58,
    },
    msgWrapper: {
        marginLeft: 5,
        paddingVertical: 7,
    },
    detailsWrapper: {
        position: 'absolute',
        right: 10,
        top: 8,
        borderLeftWidth: 0.8,
        paddingLeft: 15,
        borderLeftColor: '#cdcbcb'
    },
    detailsButton: {
        // borderWidth: 1,
        borderRadius: 12,
        width: 60,
        height: 56,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#f76b10',
    },
    details: {
        textAlign: 'center',
        color: 'white'
    },
    msgTitle: {
        fontSize: 19,
        fontWeight: 'bold',
        letterSpacing: 0.1,
        marginTop: 3,
    },
    metaWrapper: {
        flexDirection: 'row',
        width: '100%',
    },
    meta: {
        fontSize: 15,
        fontWeight: '300',
        color: 'gray',
        marginRight: 10,
        letterSpacing: 0.01
    }
});

export default RecommendationCardComponent;