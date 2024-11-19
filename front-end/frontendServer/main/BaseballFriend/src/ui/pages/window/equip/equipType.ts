export interface EquipInfo{
    characterList : [
        {characterSerialNumber : number;
        characterName : string;
        itemList : [{
            itemSerialNumber : number;
            itemCategory : number;
            teamName : string;
        }] | null,}
    ],
    backgroundList : [
        {
            backgroundSerialNumber: number;
        }
    ] | null,
    characterSerialNumber: number,
    headItemSerialNumber: number,
    bodyItemSerialNumber: number,
    armItemSerialNumber: number,
    backgroundSerialNumber: number,
}

export interface MoneyData{
    gameMoney: number;
}

export interface UserItems{
    characterInfoSerialNumber: number,
    headItemSerialNumber: number,
    bodyItemSerialNumber: number,
    armItemSerialNumber: number,
    backgroundSerialNumber: number,
}

export interface ShopItems{
    characterList : [{
        characterSerialNumber: number,
        characterName: string,
    }
    ],
    itemList : [{ itemSerialNumber: number; characterSerialNumber: number; itemCategory: number; price: number; teamName: String; }],
    backgroundList : [] | null,
}