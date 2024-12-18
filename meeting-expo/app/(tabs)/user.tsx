import React, {useCallback, useRef,useState} from "react";
import {StyleSheet, Image, Platform, Text, TouchableOpacity, View} from 'react-native';
import {ThemedView} from "@/components/ThemedView";
import {ThemedText} from "@/components/ThemedText";

/**
 @name:用户数据页面
 @description:提供用户信息预览展示和系统入口
 @author: xiye.Cayon
 @time: 2024-12-16 13:04:46
 */
export default function UserAppScreen() {


    return (
        <ThemedView
           style={{
               flex: 1,
               // backgroundColor: 'gray',
               alignItems:'center',
               justifyContent: 'center'
           }}
        >

            <ThemedText>hello</ThemedText>


            <ThemedText>个人介绍</ThemedText>
            <ThemedText>浏览记录</ThemedText>
            <ThemedText>点赞记录</ThemedText>
            <ThemedText>收藏记录</ThemedText>

            <ThemedText>语言收藏</ThemedText>
            <ThemedText></ThemedText>




            <ThemedText style={styles.bottom}>
                GET
            </ThemedText>




        </ThemedView>
    );
}

const styles = StyleSheet.create({
    headerImage: {
        color: '#808080',
        bottom: -90,
        left: -35,
        position: 'absolute',
    },
    titleContainer: {
        flexDirection: 'row',
        gap: 8,
    },
    shadow: {
        marginTop: 20,
        alignItems: 'center',
        justifyContent: 'center',
        shadowColor: '#000',
        shadowOffset: {
            width: 0,
            height: 5
        },
        shadowOpacity: .55,
        shadowRadius: 6.84,
        elevation: 5,
    },
    image: {
        width: '90%',
        height: 400,
        resizeMode: 'cover',
        alignSelf: 'center',
        borderRadius: 20
    },
    bottom: {
        marginTop: 20,
        backgroundColor: '#f4f4f4',
        width: 80,
        height: 30,
        alignItems: 'center',
        justifyContent: 'center',
        alignSelf: 'center',
        borderRadius: 15,
        textAlign: 'center',

    }
});
