/*
 * Copyright (c) 2007 NTT DATA Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.terasoluna.fw.validation;

import java.util.ArrayList;

import jp.terasoluna.utlib.LogUTUtil;
import jp.terasoluna.utlib.UTUtil;
import junit.framework.TestCase;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.Var;

/**
 * {@link jp.terasoluna.fw.validation.FieldChecks} クラスのブラックボックステスト。
 *
 * <p>
 * <h4>【クラスの概要】</h4>
 * TERASOLUNAの入力チェック機能で共通に使用される検証ルールクラス。
 * <p>
 *
 * @see jp.terasoluna.fw.validation.FieldChecks
 */
public class FieldChecksTest06 extends TestCase {

    /**
     * このテストケースを実行する為の
     * GUI アプリケーションを起動する。
     *
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        junit.swingui.TestRunner.run(FieldChecksTest06.class);
    }

    /**
     * 初期化処理を行う。
     *
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        LogUTUtil.flush();
    }

    /**
     * 終了処理を行う。
     *
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * コンストラクタ。
     *
     * @param name このテストケースの名前。
     */
    public FieldChecksTest06(String name) {
        super(name);
    }

    /**
     * testValidateAlphaNumericString01()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) bean:null<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 引数のbeanがnullの場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateAlphaNumericString01() throws Exception {
        // 前処理
        // bean : null
        Object bean = null;
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();
        
        // テスト実施
        boolean b =
            new FieldChecks().validateAlphaNumericString(bean, va, field, errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateAlphaNumericString02()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) bean:""<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 引数のbeanが空文字の場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateAlphaNumericString02() throws Exception {
        // 前処理
        // bean : ""
        Object bean = "";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();
        
        // テスト実施
        boolean b =
            new FieldChecks().validateAlphaNumericString(bean, va, field, errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateAlphaNumericString03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) bean:"a0A"<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 引数のbeanが半角英数文字のみで構成されている場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateAlphaNumericString03() throws Exception {
        // 前処理
        // bean : "a0A"
        Object bean = "a0A";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();
        
        // テスト実施
        boolean b =
            new FieldChecks().validateAlphaNumericString(bean, va, field, errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateAlphaNumericString04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) bean:"Zg3%"<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     *
     * <br>
     * 引数のbeanに半角英数文字以外の文字が含まれている場合、エラーを追加してtrueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateAlphaNumericString04() throws Exception {
        // 前処理
        // bean : "Zg3%"
        Object bean = "Zg3%";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();
        
        // テスト実施
        boolean b = new FieldChecks().validateAlphaNumericString(bean, va, field, errors);

        // 判定
        assertFalse(b);
        // 呼出確認
        assertEquals(1, errors.addErrorCount);
        // 引数確認
        ArrayList beanList = (ArrayList) UTUtil.getPrivateField(errors, "beanList");
        assertSame(bean, beanList.get(0));
        ArrayList vaList = (ArrayList) UTUtil.getPrivateField(errors, "vaList");
        assertSame(va, vaList.get(0));
        ArrayList fieldList = (ArrayList) UTUtil.getPrivateField(errors, "fieldList");
        assertSame(field, fieldList.get(0));
    }

    /**
     * testValidateCapAlphaNumericString01()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) bean:null<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 引数のbeanがnullの場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateCapAlphaNumericString01() throws Exception {
        // 前処理
        // bean : null
        Object bean = null;
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();
        
        // テスト実施
        boolean b =
            new FieldChecks().validateCapAlphaNumericString(bean, va, field, errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateCapAlphaNumericString02()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) bean:""<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 引数のbeanが空文字の場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateCapAlphaNumericString02() throws Exception {
        // 前処理
        // bean : ""
        Object bean = "";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();
        
        // テスト実施
        boolean b =
            new FieldChecks().validateCapAlphaNumericString(bean, va, field, errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateCapAlphaNumericString03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) bean:"ABC0"<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 引数のbeanが大文字の半角英数文字のみで構成されている場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateCapAlphaNumericString03() throws Exception {
        // 前処理
        // bean : "ABC0"
        Object bean = "ABC0";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();
        
        // テスト実施
        boolean b =
            new FieldChecks().validateCapAlphaNumericString(bean, va, field, errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateCapAlphaNumericString04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) bean:"Aa0"<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     *
     * <br>
     * 引数のbeanに大文字の半角英数文字以外の文字が含まれている場合、エラーを追加してtrueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateCapAlphaNumericString04() throws Exception {
        // 前処理
        // bean : "Aa0"
        Object bean = "Aa0";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();
        
        // テスト実施
        boolean b = new FieldChecks().validateCapAlphaNumericString(bean, va, field, errors);

        // 判定
        assertFalse(b);
        // 呼出確認
        assertEquals(1, errors.addErrorCount);
        // 引数確認
        ArrayList beanList = (ArrayList) UTUtil.getPrivateField(errors, "beanList");
        assertSame(bean, beanList.get(0));
        ArrayList vaList = (ArrayList) UTUtil.getPrivateField(errors, "vaList");
        assertSame(va, vaList.get(0));
        ArrayList fieldList = (ArrayList) UTUtil.getPrivateField(errors, "fieldList");
        assertSame(field, fieldList.get(0));
    }

    /**
     * testValidateNumber01()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) bean:null<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 引数のbeanがnullの場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateNumber01() throws Exception {
        // 前処理
        // bean : null
        Object bean = null;
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();
        
        // テスト実施
        boolean b =
            new FieldChecks().validateNumber(bean, va, field, errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateNumber02()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) bean:""<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 引数のbeanが空文字の場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateNumber02() throws Exception {
        // 前処理
        // bean : ""
        Object bean = "";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();
        
        // テスト実施
        boolean b =
            new FieldChecks().validateNumber(bean, va, field, errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateNumber03()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) bean:"５"（全角）<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     *
     * <br>
     * 入力値が全角の場合、エラーを追加しfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateNumber03() throws Exception {
        // 前処理
        // bean : "５"
        Object bean = "５";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();
        
        // テスト実施
        boolean b =
            new FieldChecks().validateNumber(bean, va, field, errors);

        // 判定
        assertFalse(b);
        // 呼出確認
        assertEquals(1, errors.addErrorCount);
        // 引数確認
        ArrayList beanList = (ArrayList) UTUtil.getPrivateField(errors, "beanList");
        assertSame(bean, beanList.get(0));
        ArrayList vaList = (ArrayList) UTUtil.getPrivateField(errors, "vaList");
        assertSame(va, vaList.get(0));
        ArrayList fieldList = (ArrayList) UTUtil.getPrivateField(errors, "fieldList");
        assertSame(field, fieldList.get(0));
    }

    /**
     * testValidateNumber04()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) bean:"test"<br>
     *         (引数) va:not null<br>
     *         (引数) field:not null<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     *
     * <br>
     * 入力値が数値に変換できない場合、エラーを追加しfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateNumber04() throws Exception {
        // 前処理
        // bean : "test"
        Object bean = "test";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : not null
        Field field = new Field();
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();
        
        // テスト実施
        boolean b =
            new FieldChecks().validateNumber(bean, va, field, errors);

        // 判定
        assertFalse(b);
        // 呼出確認
        assertEquals(1, errors.addErrorCount);
        // 引数確認
        ArrayList beanList = (ArrayList) UTUtil.getPrivateField(errors, "beanList");
        assertSame(bean, beanList.get(0));
        ArrayList vaList = (ArrayList) UTUtil.getPrivateField(errors, "vaList");
        assertSame(va, vaList.get(0));
        ArrayList fieldList = (ArrayList) UTUtil.getPrivateField(errors, "fieldList");
        assertSame(field, fieldList.get(0));
    }

    /**
     * testValidateNumber05()
     * <br><br>
     *
     * (異常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) bean:"5"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                integerLength="abc"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Mistake on validation definition file. - integerLength is not number. You'll have to check it over. "<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Mistake on validation definition file. - integerLength is not number. You'll have to check it over. ", new NumberFormatException()<br>
     *
     * <br>
     * varのintegerLengthが数値に変換できない場合、ValidatorExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateNumber05() throws Exception {
        // 前処理
        // bean : "5"
        Object bean = "5";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : var:integerLength="abc"
        Field field = new Field();
        Var var = new Var();
        var.setName("integerLength");
        var.setValue("abc");
        field.addVar(var);
        
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();
        
        try {
            // テスト実施
            @SuppressWarnings("unused") boolean b =
                new FieldChecks().validateNumber(bean, va, field, errors);
            fail();
        } catch (ValidatorException e) {
            // 判定
            String message = "Mistake on validation definition file. "
                + "- integerLength is not number. "
                + "You'll have to check it over. ";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message, new NumberFormatException()));
        }
    }

    /**
     * testValidateNumber06()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：G
     * <br><br>
     * 入力値：(引数) bean:"5"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                scale="abc"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(状態変化) 例外:ValidatorException<br>
     *                    メッセージ："Mistake on validation definition file. - scale is not number. You'll have to check it over. "<br>
     *         (状態変化) ログ:ログレベル：エラー<br>
     *                    メッセージ："Mistake on validation definition file. - scale is not number. You'll have to check it over. ", new NumberFormatException()<br>
     *
     * <br>
     * varのscaleが数値に変換できない場合、ValidatorExceptionが発生することを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateNumber06() throws Exception {
        // 前処理
        // bean : "5"
        Object bean = "5";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : var:scale="abc"
        Field field = new Field();
        Var var = new Var();
        var.setName("scale");
        var.setValue("abc");
        field.addVar(var);
        
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();
        
        try {
            // テスト実施
            @SuppressWarnings("unused") boolean b =
                new FieldChecks().validateNumber(bean, va, field, errors);
            fail();
        } catch (ValidatorException e) {
            // 判定
            String message = "Mistake on validation definition file. "
                + "- scale is not number. "
                + "You'll have to check it over. ";
            assertEquals(message, e.getMessage());
            assertTrue(LogUTUtil.checkError(message, new NumberFormatException()));
        }
    }

    /**
     * testValidateNumber07()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：
     * <br><br>
     * 入力値：(引数) bean:"100.05"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                integerLength="5"<br>
     *                scale="3"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * 入力された文字の整数部の桁数が、varのintegerLengthの値より小さく、小数部の桁数がvarのscaleの値より小さい場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateNumber07() throws Exception {
        // 前処理
        // bean : "100.05"
        Object bean = "100.05";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : var:integerLength="5" scale="3"
        Field field = new Field();
        Var var1 = new Var();
        var1.setName("integerLength");
        var1.setValue("5");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("scale");
        var2.setValue("3");
        field.addVar(var2);
        
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();
        
        // テスト実施
        boolean b =
            new FieldChecks().validateNumber(bean, va, field, errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateNumber08()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) bean:"100.05"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                integerLength="3"<br>
     *                scale="2"<br>
     *                isAccordedInteger="true"<br>
     *                isAccordedScale="true"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * varのisAccordedIntegerにtrueが指定されていて、入力された文字の整数部の桁数が、varのintegerLengthの値と等しく、varのisAccordedScaleにtrueが指定されていて、小数部の桁数がvarのscaleの値と等しい場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateNumber08() throws Exception {
        // 前処理
        // bean : "100.05"
        Object bean = "100.05";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : var:integerLength="3" scale="2" isAccordedInteger="true" isAccordedScale="true"
        Field field = new Field();
        Var var1 = new Var();
        var1.setName("integerLength");
        var1.setValue("3");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("scale");
        var2.setValue("2");
        field.addVar(var2);
        
        Var var3 = new Var();
        var3.setName("isAccordedInteger");
        var3.setValue("true");
        field.addVar(var3);
        
        Var var4 = new Var();
        var4.setName("isAccordedScale");
        var4.setValue("true");
        field.addVar(var4);
        
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();
        
        // テスト実施
        boolean b =
            new FieldChecks().validateNumber(bean, va, field, errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateNumber09()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) bean:"100.05"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                integerLength="5"<br>
     *                scale="3"<br>
     *                isAccordedInteger="test"<br>
     *                isAccordedScale="test"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:true<br>
     *         (状態変化) errors:呼び出されない<br>
     *
     * <br>
     * varのisAccordedIntegerにtrue以外の文字列が指定されていて、入力された文字の整数部の桁数が、varのintegerLengthの値より小さく、varのisAccordedScaleにtrue以外の文字列が指定されていて、小数部の桁数がvarのscaleの値より小さい場合、trueが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateNumber09() throws Exception {
        // 前処理
        // bean : "100.05"
        Object bean = "100.05";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : var:integerLength="5" scale="3" isAccordedInteger="test" isAccordedScale="test"
        Field field = new Field();
        
        Var var1 = new Var();
        var1.setName("integerLength");
        var1.setValue("5");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("scale");
        var2.setValue("3");
        field.addVar(var2);
        
        Var var3 = new Var();
        var3.setName("isAccordedInteger");
        var3.setValue("test");
        field.addVar(var3);
        
        Var var4 = new Var();
        var4.setName("isAccordedScale");
        var4.setValue("test");
        field.addVar(var4);
        
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();
        
        // テスト実施
        boolean b =
            new FieldChecks().validateNumber(bean, va, field, errors);

        // 判定
        assertTrue(b);
        assertEquals(0, errors.addErrorCount);
    }

    /**
     * testValidateNumber10()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) bean:"100.05"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                integerLength="2"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     *
     * <br>
     * 入力された文字の整数部の桁数が、varのintegerLengthの値より大きい場合、エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateNumber10() throws Exception {
        // 前処理
        // bean : "100.05"
        Object bean = "100.05";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        
        // field : var:integerLength="2"
        Field field = new Field();
        Var var = new Var();
        var.setName("integerLength");
        var.setValue("2");
        field.addVar(var);
        
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();
        
        // テスト実施
        boolean b =
            new FieldChecks().validateNumber(bean, va, field, errors);
        
        // 判定
        assertFalse(b);
        // 呼出確認
        assertEquals(1, errors.addErrorCount);
        // 引数確認
        ArrayList beanList = (ArrayList) UTUtil.getPrivateField(errors, "beanList");
        assertSame(bean, beanList.get(0));
        ArrayList vaList = (ArrayList) UTUtil.getPrivateField(errors, "vaList");
        assertSame(va, vaList.get(0));
        ArrayList fieldList = (ArrayList) UTUtil.getPrivateField(errors, "fieldList");
        assertSame(field, fieldList.get(0));
    }

    /**
     * testValidateNumber11()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) bean:"100.05"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                scale="1"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     *
     * <br>
     * 入力された文字の小数部の桁数が、varのscaleの値より大きい場合、エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateNumber11() throws Exception {
        // 前処理
        // bean : "100.05"
        Object bean = "100.05";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        
        // field : var:scale="1"
        Field field = new Field();
        Var var = new Var();
        var.setName("scale");
        var.setValue("1");
        field.addVar(var);
        
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();
        
        // テスト実施
        boolean b =
            new FieldChecks().validateNumber(bean, va, field, errors);
        
        // 判定
        assertFalse(b);
        // 呼出確認
        assertEquals(1, errors.addErrorCount);
        // 引数確認
        ArrayList beanList = (ArrayList) UTUtil.getPrivateField(errors, "beanList");
        assertSame(bean, beanList.get(0));
        ArrayList vaList = (ArrayList) UTUtil.getPrivateField(errors, "vaList");
        assertSame(va, vaList.get(0));
        ArrayList fieldList = (ArrayList) UTUtil.getPrivateField(errors, "fieldList");
        assertSame(field, fieldList.get(0));
    }

    /**
     * testValidateNumber12()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) bean:"100.05"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                integerLength="5"<br>
     *                isAccordedInteger="true"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     *
     * <br>
     * varのisAccordedIntegerにtrueが指定されていて、入力された文字の整数部の桁数が、varのintegerLengthの値より小さい場合、エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateNumber12() throws Exception {
        // 前処理
        // bean : "100.05"
        Object bean = "100.05";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : var:scale="5" isAccordedInteger="true"
        Field field = new Field();
        Var var1 = new Var();
        var1.setName("scale");
        var1.setValue("5");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("isAccordedInteger");
        var2.setValue("true");
        field.addVar(var2);
        
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();
        
        // テスト実施
        boolean b =
            new FieldChecks().validateNumber(bean, va, field, errors);
        
        // 判定
        assertFalse(b);
        // 呼出確認
        assertEquals(1, errors.addErrorCount);
        // 引数確認
        ArrayList beanList = (ArrayList) UTUtil.getPrivateField(errors, "beanList");
        assertSame(bean, beanList.get(0));
        ArrayList vaList = (ArrayList) UTUtil.getPrivateField(errors, "vaList");
        assertSame(va, vaList.get(0));
        ArrayList fieldList = (ArrayList) UTUtil.getPrivateField(errors, "fieldList");
        assertSame(field, fieldList.get(0));
    }

    /**
     * testValidateNumber13()
     * <br><br>
     *
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) bean:"100.05"<br>
     *         (引数) va:not null<br>
     *         (引数) field:var:<br>
     *                scale="3"<br>
     *                isAccordedScale="true"<br>
     *         (引数) errors:not null<br>
     *
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     *         (状態変化) errors:bean,field,vaを引数としてaddErrorsが呼び出される。<br>
     *
     * <br>
     * varのisAccordedScaleにtrueが指定されていて、入力された文字の小数部の桁数が、varのscaleの値より小さい場合、エラーを追加してfalseが返却されることを確認する。
     * <br>
     *
     * @throws Exception このメソッドで発生した例外
     */
    public void testValidateNumber13() throws Exception {
        // 前処理
        // bean : "100.05"
        Object bean = "100.05";
        // va : not null
        ValidatorAction va = new ValidatorAction();
        // field : var:scale="3" isAccordedScale="true"
        Field field = new Field();
        Var var1 = new Var();
        var1.setName("scale");
        var1.setValue("3");
        field.addVar(var1);
        
        Var var2 = new Var();
        var2.setName("isAccordedScale");
        var2.setValue("true");
        field.addVar(var2);
        
        // errors : not null
        FieldChecks_ValidationErrorsImpl01 errors =
            new FieldChecks_ValidationErrorsImpl01();
        
        // テスト実施
        boolean b =
            new FieldChecks().validateNumber(bean, va, field, errors);
        
        // 判定
        assertFalse(b);
        // 呼出確認
        assertEquals(1, errors.addErrorCount);
        // 引数確認
        ArrayList beanList = (ArrayList) UTUtil.getPrivateField(errors, "beanList");
        assertSame(bean, beanList.get(0));
        ArrayList vaList = (ArrayList) UTUtil.getPrivateField(errors, "vaList");
        assertSame(va, vaList.get(0));
        ArrayList fieldList = (ArrayList) UTUtil.getPrivateField(errors, "fieldList");
        assertSame(field, fieldList.get(0));
    }
}