package br.com.clash_utilities.utils;

import br.com.clash_utilities.model.PlayerData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;

public class ExcelGenerator {

    public void generatePlayerDataExcel(List<PlayerData> playerDataList, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Player Data");

            // Criação do estilo para centralizar os cabeçalhos e valores
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle valueStyleWhite = workbook.createCellStyle();
            valueStyleWhite.setAlignment(HorizontalAlignment.CENTER);
            valueStyleWhite.setVerticalAlignment(VerticalAlignment.CENTER);
            valueStyleWhite.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            valueStyleWhite.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle valueStyleGray = workbook.createCellStyle();
            valueStyleGray.setAlignment(HorizontalAlignment.CENTER);
            valueStyleGray.setVerticalAlignment(VerticalAlignment.CENTER);
            valueStyleGray.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            valueStyleGray.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Criação da linha de cabeçalho superior com "Guerra 1", "Guerra 2", etc.
            Row warHeaderRow = sheet.createRow(0);
            for (int i = 2; i <= 14; i += 2) {
                Cell cell = warHeaderRow.createCell(i);
                cell.setCellValue("Guerra " + (i / 2));
                cell.setCellStyle(headerStyle);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, i, i + 1));
            }

            // Criação da linha de cabeçalho com os detalhes
            String[] headers = {"Tag", "Name", "Ataque", "Defesa",
                    "Ataque", "Defesa", "Ataque", "Defesa",
                    "Ataque", "Defesa", "Ataque", "Defesa",
                    "Ataque", "Defesa", "Ataque", "Defesa",
                    "ATK", "DEF", "TOTAL"};
            Row headerRow = sheet.createRow(1);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Preenchimento dos dados
            int rowNum = 2; // Começa na terceira linha
            for (PlayerData player : playerDataList) {
                Row row = sheet.createRow(rowNum);
                // Alterna o estilo de cor conforme a linha
                CellStyle valueStyle = (rowNum % 2 == 0) ? valueStyleWhite : valueStyleGray;

                row.createCell(0).setCellValue(player.getTag());
                row.getCell(0).setCellStyle(valueStyle);
                row.createCell(1).setCellValue(player.getName());
                row.getCell(1).setCellStyle(valueStyle);

                Cell attackCell1 = row.createCell(2);
                attackCell1.setCellValue(player.getWar1().getAttackStars());
                attackCell1.setCellStyle(valueStyle);

                Cell defenseCell1 = row.createCell(3);
                defenseCell1.setCellValue(player.getWar1().getDefenseStars());
                defenseCell1.setCellStyle(valueStyle);

                Cell attackCell2 = row.createCell(4);
                attackCell2.setCellValue(player.getWar2().getAttackStars());
                attackCell2.setCellStyle(valueStyle);

                Cell defenseCell2 = row.createCell(5);
                defenseCell2.setCellValue(player.getWar2().getDefenseStars());
                defenseCell2.setCellStyle(valueStyle);

                Cell attackCell3 = row.createCell(6);
                attackCell3.setCellValue(player.getWar3().getAttackStars());
                attackCell3.setCellStyle(valueStyle);

                Cell defenseCell3 = row.createCell(7);
                defenseCell3.setCellValue(player.getWar3().getDefenseStars());
                defenseCell3.setCellStyle(valueStyle);

                Cell attackCell4 = row.createCell(8);
                attackCell4.setCellValue(player.getWar4().getAttackStars());
                attackCell4.setCellStyle(valueStyle);

                Cell defenseCell4 = row.createCell(9);
                defenseCell4.setCellValue(player.getWar4().getDefenseStars());
                defenseCell4.setCellStyle(valueStyle);

                Cell attackCell5 = row.createCell(10);
                attackCell5.setCellValue(player.getWar5().getAttackStars());
                attackCell5.setCellStyle(valueStyle);

                Cell defenseCell5 = row.createCell(11);
                defenseCell5.setCellValue(player.getWar5().getDefenseStars());
                defenseCell5.setCellStyle(valueStyle);

                Cell attackCell6 = row.createCell(12);
                attackCell6.setCellValue(player.getWar6().getAttackStars());
                attackCell6.setCellStyle(valueStyle);

                Cell defenseCell6 = row.createCell(13);
                defenseCell6.setCellValue(player.getWar6().getDefenseStars());
                defenseCell6.setCellStyle(valueStyle);

                Cell attackCell7 = row.createCell(14);
                attackCell7.setCellValue(player.getWar7().getAttackStars());
                attackCell7.setCellStyle(valueStyle);

                Cell defenseCell7 = row.createCell(15);
                defenseCell7.setCellValue(player.getWar7().getDefenseStars());
                defenseCell7.setCellStyle(valueStyle);

                Cell totalAttackStarsCell = row.createCell(16);
                totalAttackStarsCell.setCellValue(player.getTotalAttackStars());
                totalAttackStarsCell.setCellStyle(valueStyle);

                Cell totalDefenseStarsCell = row.createCell(17);
                totalDefenseStarsCell.setCellValue(player.getTotalDefenseStars());
                totalDefenseStarsCell.setCellStyle(valueStyle);

                Cell totalStarsCell = row.createCell(18);
                totalStarsCell.setCellValue(player.getTotalStars());
                totalStarsCell.setCellStyle(valueStyle);

                rowNum++;
            }

            // Ajusta automaticamente o tamanho das colunas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Salva o arquivo Excel
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            System.out.println("Excel gerado com sucesso em: " + filePath);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar o arquivo Excel", e);
        }
    }
}