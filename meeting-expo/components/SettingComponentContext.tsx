import {createContext} from "react";
import {SharedValue} from "react-native-reanimated";

interface ISettingComponentContextProps {
    active: SharedValue<boolean>
}


const SettingContext = createContext<any>(null);


export default SettingContext;