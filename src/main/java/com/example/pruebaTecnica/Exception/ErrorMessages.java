package com.example.pruebaTecnica.Exception;

public class ErrorMessages {
    public static final String CLIENT_NOT_FOUND = "Cliente no encontrado";
    public static final String NAME_MIN_LENGTH_REQUIRED = "El nombre debe tener como minimo dos caracteres";
    public static final String LASTNAME_MIN_LENGTH_REQUIRED = "El apellido debe tener como minimo dos caracteres";
    public static final String EMAIL_NOT_EMPTY = "El correo no debe estar vacio";
    public static final String EMAIL_INVALID_FORMAT = "El email debe tener un formato valido (xxxx@xxxxx.xxx)";
    public static final String DATE_OF_BIRTH_REQUIRED = "La fecha de nacimiento es obligatoria";
    public static final String CLIENT_MUST_BE_ADULT = "El cliente debe ser mayor de edad";
    public static final String ACCOUNT_TYPE_REQUIRED = "El tipo de cuenta es obligatorio";
    public static final String INVALID_ACCOUNT_TYPE = "El tipo de cuenta debe ser de ahorros o corriente";
    public static final String SAVINGS_ACCOUNT_MIN_BALANCE = "El saldo de la cuenta no puede ser menor a cero";
    public static final String PRODUCT_NOT_FOUND = "Cuenta no encontrada";
    public static final String CANNOT_CANCEL_ACCOUNT_WITH_BALANCE = "No se puede cancelar la cuenta, aun tiene saldo disponible";
    public static final String CANNOT_DELETE_ACCOUNT_WITH_BALANCE = "No se puede eliminar la cuenta, aun tiene saldo disponible";
    public static final String INVALID_AMOUNT = "El monto debe ser mayor a cero";
    public static final String ACCOUNT_NUMBER_NOT_FOUND = "No se encontro el numero de cuenta de origen";
    public static final String ACCOUNT_NOT_ACTIVE = "La cuenta de origen no esta activa";
    public static final String DESTINATION_ACCOUNT_NUMBER_NOT_FOUND = "No se encontro el numero de cuenta de destino";
    public static final String DESTINATION_ACCOUNT_NOT_ACTIVE = "La cuenta de destino no esta activa";
    public static final String CANNOT_TRANSFER_TO_SAME_ACCOUNT = "No puedes transferir a la misma cuenta";
    public static final String INSUFFICIENT_FUNDS_IN_ORIGIN_ACCOUNT = "Saldo insuficiente en la cuenta de origen";
    public static final String INVALID_TRANSACTION_TYPE = "La transacción no es valida";
    public static final String TRANSACTION_NOT_FOUND = "La transacción no existe.";
    public static final String CLIENT_HAS_LINKED_PRODUCTS = "El cliente no puede ser eliminado porque tiene productos asociados.";
}