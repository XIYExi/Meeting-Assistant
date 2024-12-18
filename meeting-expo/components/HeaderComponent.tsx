import React, {ReactNode} from "react";
import {ThemedView} from "@/components/ThemedView";
import {Dimensions, Image, Pressable, StyleSheet, View} from "react-native";
import {ThemedText} from "@/components/ThemedText";
import {CommonActions, Link, useNavigation} from "@react-navigation/native";

/**
 @name: Header组件
 @description: 每个app页面的Header推出组件，统一Header高度
 @author: xiye.Cayon
 @time: 2024-12-15 19:29:35
 */
interface IHeaderProps {
    backHref: string,
    page?: string,
    rightComponent?: ReactNode
    useRedirect: boolean
}

const {width, height} = Dimensions.get("window");
function HeaderComponent(props: IHeaderProps) {

    const navigation = useNavigation();
    const handleBackRedirect = () => {
        navigation.dispatch(
            CommonActions.reset({
                index: 0,
                routes: [{name: props.backHref}],
            })
        )
    }

    return (
        <React.Fragment>
            <ThemedView style={styles.headerContainer}>
                <ThemedView style={styles.leftContainer}>

                    {
                        props.useRedirect ? (
                            <>
                            <Pressable onPress={handleBackRedirect}>
                                <Image source={require('../assets/components/back.png')} style={styles.backIcon}/>
                            </Pressable>
                            </>
                        ) : (
                            <>
                                {/*@ts-ignore*/}
                                <Link screen={props.backHref}>
                                    <Image source={require('../assets/components/back.png')} style={styles.backIcon}/>
                                </Link>
                            </>
                        )
                    }

                </ThemedView>
                <View style={styles.centerContainer}>
                    <ThemedText style={styles.pageTitle}>{props.page && props.page || ''}</ThemedText>
                </View>
                <View style={styles.rightContainer}>
                    <ThemedView>{props.rightComponent && props.rightComponent || <React.Fragment />}</ThemedView>
                </View>
            </ThemedView>
        </React.Fragment>
    )
}


const styles = StyleSheet.create({
   headerContainer: {
       width: width,
       marginVertical: 12,
       height: height * 0.1,
       paddingHorizontal: 15,
       flexDirection: 'row',
   },
    leftContainer: {
        width: 50,
        left: 0,
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: 14,
    },
    centerContainer: {
        width: 100,
        left: width / 2 - 108,
        justifyContent: 'center',
        marginTop: 10,
    },
    rightContainer: {
        width: 50,
        left: width / 2 - 25,
        marginTop: 10,
        justifyContent: 'center',
    },
    backIcon: {
        width: 28,
        height: 28,
        //borderWidth: 1,
    },
    pageTitle: {
        marginTop: 10,
        fontSize: 22,
        fontWeight: 'bold',
    },
});



export default HeaderComponent;