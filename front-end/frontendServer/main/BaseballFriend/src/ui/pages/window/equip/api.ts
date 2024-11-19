import { instance } from '../../../api/axios';
import { EquipInfo, MoneyData, UserItems, ShopItems} from './equipType';

// 프로그램 시작 시 보유 캐릭터 장비 현황
export async function EquipmentInfo() {
    const response = await instance.get<EquipInfo>(
        '/character/status'
    );
    return response.data;
}

export async function BuyChr(characterInfoSerialNumber  : number) {
    const response = await instance.post<{ gameMoney : number }>(
        '/character/shop/character', { characterInfoSerialNumber }
    );
    return response.data
}

export async function GetmoneyData() {
    const response = await instance.get<MoneyData>(
        '/game/money'
    );
    return response.data;
}

export async function SubmitEquipitems(items: UserItems) {
    const response = await instance.post(
        '/character/customization', items
    )
    return response;
}

export async function ShopitemsInfo() {
    const response = await instance.get<ShopItems>(
        '/character/shop'
    )
    return response.data;
}

export async function GetMyteam() {
    const response = await instance.get(
        '/member/team'
    )
    return response.data;
}

export async function BuyItem(itemSerialNumber : number) {
    const response = await instance.post<{ gameMoney : number }>(
        '/character/shop/item', { itemSerialNumber }
    );
    return response.data
}