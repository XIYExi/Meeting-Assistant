import {ThemedView} from "@/components/ThemedView";
import {PlatformPressable} from "@react-navigation/elements";
import {StyleSheet, TouchableOpacity} from "react-native";
import {ThemedText} from "@/components/ThemedText";
import {AntDesign} from "@expo/vector-icons";
import {is} from "@babel/types";
import {useColorScheme} from "@/hooks/useColorScheme";
import {DarkTheme, DefaultTheme} from "@react-navigation/native";


const TabBarComponent = ({state, descriptors, navigation}: any) => {
    const primaryColor = '#F76B10';
    const greyColor = '#737373';

    const icons = {
        home: (props: any) => <AntDesign name={'home'} size={20} color={greyColor} {...props} />,
        explore: (props: any) => <AntDesign name={'pluscircleo'} size={20} color={greyColor} {...props} />,
        user: (props: any) => <AntDesign name={'user'} size={20} color={greyColor} {...props} />,
    }

    const colorScheme = useColorScheme();

    return (
        <ThemedView style={[styles.tabbar, {
            shadowColor: colorScheme === 'dark' ? 'white' : 'black',
            backgroundColor: colorScheme === 'dark' ? DarkTheme.colors.background : DefaultTheme.colors.background,
        }]}>
            {state.routes.map((route: any, index: number) => {
                const { options } = descriptors[route.key];
                const label =
                    options.tabBarLabel !== undefined
                        ? options.tabBarLabel
                        : options.title !== undefined
                            ? options.title
                            : route.name;

                if (['_sitemap', '+not-found'].includes(route.name)) return null;

                const isFocused = state.index === index;

                const onPress = () => {
                    const event = navigation.emit({
                        type: 'tabPress',
                        target: route.key,
                        canPreventDefault: true,
                    });

                    if (!isFocused && !event.defaultPrevented) {
                        navigation.navigate(route.name, route.params);
                    }
                };

                const onLongPress = () => {
                    navigation.emit({
                        type: 'tabLongPress',
                        target: route.key,
                    });
                };

                return (
                    <TouchableOpacity
                        key={`TabBarComponent-${index}`}
                        accessibilityState={isFocused ? { selected: true } : {}}
                        accessibilityLabel={options.tabBarAccessibilityLabel}
                        testID={options.tabBarButtonTestID}
                        onPress={onPress}
                        onLongPress={onLongPress}
                        style={styles.tabbarItem}
                    >
                        {
                            // @ts-ignore
                            icons[route.name]({
                                color: isFocused? primaryColor: greyColor,
                            })
                        }
                        <ThemedText style={{ color: isFocused ? primaryColor : greyColor, fontSize: 14 }}>
                            {label}
                        </ThemedText>
                    </TouchableOpacity>
                );
            })}
        </ThemedView>
    )
}

const styles = StyleSheet.create({
    tabbar: {
        position: 'absolute',
        bottom: 25,
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginHorizontal: 20,
        paddingVertical: 12,
        borderRadius: 25,
        borderCurve: 'continuous',
        shadowRadius: 10,
        shadowOffset: {width: 0, height: 10},
        shadowOpacity: .1,
        zIndex: 888,
    },
    tabbarItem: {
        flex: 1,
        zIndex: 777,
        justifyContent: 'center',
        alignItems: 'center'
    }
});


export default TabBarComponent;