import {ThemedView} from "@/components/ThemedView";
import {StyleSheet} from "react-native";
import React from "react";

/**
 @name:页面容器组件，撑开左右padding
 @description:
 @author: xiye.Cayon
 @time: 2024-12-15 20:28:11
 */
interface IPageProps {
    children?: React.ReactNode;
}

function PageComponent(props: IPageProps) {
    return (
        <ThemedView style={styles.pageContainer}>
            {props.children}
        </ThemedView>
    )
}

const styles = StyleSheet.create({
    pageContainer: {
        paddingHorizontal: 20,
        // borderWidth: 1
    }
});

export default PageComponent;