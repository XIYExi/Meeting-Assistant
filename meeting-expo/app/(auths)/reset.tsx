import React, {useState} from "react";
import {Pressable, SafeAreaView, StyleSheet} from "react-native";
import {ThemedView} from "@/components/ThemedView";
import HeaderComponent from "@/components/HeaderComponent";
import PageComponent from "@/components/PageComponent";
import {ThemedText} from "@/components/ThemedText";
import InputComponent from "@/components/InputComponent";
import ButtonComponent from "@/components/ButtonComponent";
import {CommonActions, useNavigation} from "@react-navigation/native";


/**
 @name:重设密码页面
 @description:重设密码和确认密码
 @author: xiye.Cayon
 @time: 2024-12-15 22:53:31
 */
interface IResetProps {

}

function ResetPasswordApp(props: IResetProps) {

    const [password, setPassword] = useState<string>('');
    const [confirmPWD, setConfirmPWD] = useState<string>('');

    const navigation = useNavigation();

    const handleResetPWD = () => {

        // todo 验证两次是否正确

        navigation.dispatch(
            CommonActions.reset({
                index: 0,
                routes: [{name: 'login'}],
            })
        );
    }


    return (
        <React.Fragment>
            <SafeAreaView style={{flex: 1}}>
                <ThemedView style={{flex: 1}}>

                    <HeaderComponent
                        backHref={'login'}
                        useRedirect={true}
                        page='忘记密码'
                    />

                    <PageComponent>
                        <ThemedText style={styles.title}>创建新密码🔒</ThemedText>
                        <ThemedText style={styles.subTitle}>
                            创建新密码。如果你忘记了它，那么你必须做忘记密码。
                        </ThemedText>

                        <ThemedView style={{marginTop: 20}}>
                            <InputComponent
                                secure
                                label='新密码'
                                style={[styles.input]}
                                onChangeText={(text: string) => setPassword(text)}
                            />

                            <InputComponent
                                secure
                                label='确认密码'
                                style={[styles.input]}
                                onChangeText={(text: string) => setConfirmPWD(text)}
                            />

                            <Pressable onPress={handleResetPWD}>
                                <ButtonComponent
                                    style={{
                                        borderRadius: 100,
                                        marginTop: 25,
                                    }}
                                    text={'继续'}
                                >
                                </ButtonComponent>
                            </Pressable>
                        </ThemedView>
                    </PageComponent>
                </ThemedView>
            </SafeAreaView>
        </React.Fragment>
    )
}
const styles = StyleSheet.create({
    title: {
        marginTop: 10,
        color: '#212121',
        fontSize: 28,
        fontStyle: 'normal',
        fontWeight: '600',
        lineHeight: 50,
    },
    subTitle: {
        marginTop: 16,
        color: '#212121',
        fontSize: 16,
        fontStyle: 'normal',
        fontWeight: '400',
        lineHeight: 28.8,
        letterSpacing: 0.2,
    },
    input: {
        borderRadius: 0,
        borderWidth: 0,
        borderBottomColor: "#C5CCD6",
        borderBottomWidth: StyleSheet.hairlineWidth
    },
    hasErrors: {
        borderBottomColor: "#F3534A",
    }
})


export default ResetPasswordApp;