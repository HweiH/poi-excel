package org.well.test.poiexcel;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.well.test.poiexcel.factory.AbstractFactory;
import org.well.test.poiexcel.factory.FactoryProducer;
import org.well.test.poiexcel.factory.Matter;
import org.well.test.poiexcel.product.IProduct;

/**
 * @ClassName:FactoryTest
 * @Description:工厂测试
 * @author well
 * @date:2020年4月10日
 *
 */
public class FactoryTest {

    /**
     * 测试工厂
     *
     * @param args
     * @throws IOException 
     * @throws InvalidFormatException 
     * @throws EncryptedDocumentException 
     */
    public static void main(String[] args) throws EncryptedDocumentException, InvalidFormatException, IOException {
        
        //Workbook workbook = WorkbookFactory.create(new File("D:\\test\\excel\\G25流动性覆盖率和净稳定资金比例情况表第I部分：流动性覆盖率.xlsx"));
        Workbook workbook = WorkbookFactory.create(new File("D:\\test\\excel\\G25流动性覆盖率和净稳定资金比例情况表第II部分：净稳定资金比例.xlsx"));
        Sheet sheet = workbook.getSheetAt(0);
        Matter matter = new Matter(sheet);
        
        AbstractFactory factory = null;
        List<? extends IProduct> products = null;
        
//        // 明确指定工厂类型
//        factory = FactoryProducer.getFactory(FactoryType.STANDARD);
//        // 指定原材料对象
//        products = factory.getProduct(matter);
//        
//        for (IProduct product : products) {
//            System.out.println(product);
//        }
        
        // 根据原材料确定工厂类型
        factory = FactoryProducer.getFactory(matter);
        // 指定原材料对象
        products = factory.getProduct(matter);
        
        for (IProduct product : products) {
            System.out.println(product);
        }
    }

}
