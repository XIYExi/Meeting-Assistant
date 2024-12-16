import React, {useCallback, useRef,useState} from "react";
import {StyleSheet, Image, Platform, Text, TouchableOpacity, View} from 'react-native';
import ParallaxScrollView from '@/components/ParallaxScrollView';
import { IconSymbol } from '@/components/ui/IconSymbol';
import BottomSheet, {BottomSheetView} from "@gorhom/bottom-sheet";
import {ThemedView} from "@/components/ThemedView";
import {ThemedText} from "@/components/ThemedText";

/**
 @name:用户数据页面
 @description:提供用户信息预览展示和系统入口
 @author: xiye.Cayon
 @time: 2024-12-16 13:04:46
 */
export default function UserAppScreen() {

    const sheetRef = useRef<BottomSheet>(null);
    const [isOpen, setIsOpen] = useState<boolean>(true);

    const snapPoints = ["40%"];
    const handleSnapPress = useCallback((index: number) => {
        sheetRef.current?.snapToIndex(index);
        setIsOpen(true);
    }, []);

    return (
        <ThemedView
           style={{
               flex: 1,
               // backgroundColor: 'gray',
               alignItems:'center',
               justifyContent: 'center'
           }}
        >
            <View style={styles.image}>
                <Image style={[styles.image, {opacity: isOpen ? .2 : 1}]} source={{uri: ''}} />
            </View>


            <TouchableOpacity onPress={() => handleSnapPress(0)}>
                <ThemedText style={styles.bottom}>
                    GET
                </ThemedText>
            </TouchableOpacity>


            {/* todo 测试组件*/}
            <BottomSheet
                ref={sheetRef}
                snapPoints={snapPoints}
                enablePanDownToClose={true}
                onClose={() => setIsOpen(false)}
            >
                <BottomSheetView>
                    <Text>Hello</Text>
                </BottomSheetView>
            </BottomSheet>

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
