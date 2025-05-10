package br.com.clash_utilities.utils;


import br.com.clash_utilities.model.ClanWarLeagueWarAttacks;
import br.com.clash_utilities.model.ClanWarLeagueWarMembers;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExporter2 {
    public File exportToExcel(
            List<ClanWarLeagueWarMembers> firstMembers,
            List<ClanWarLeagueWarMembers> secondMembers,
            List<ClanWarLeagueWarMembers> thirdMembers,
            List<ClanWarLeagueWarMembers> fourthMembers,
            List<ClanWarLeagueWarMembers> fifthMembers,
            List<ClanWarLeagueWarMembers> sixthMembers,
            List<ClanWarLeagueWarMembers> seventhMembers,
            int warInPreparation,
            String nomeClan) {

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Members Data");

            // Criar estilo para centralização
            CellStyle centeredStyle = workbook.createCellStyle();
            centeredStyle.setAlignment(HorizontalAlignment.CENTER);
            centeredStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            //Criar estilo para lateralizacao a esquerda
            CellStyle leftStyle = workbook.createCellStyle();
            leftStyle.setAlignment(HorizontalAlignment.LEFT);

            // Criar cabeçalhos
            Row headerRow1 = sheet.createRow(0);
            Row headerRow2 = sheet.createRow(1);

            // Mesclar células
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0)); // Tag (A1:A2)
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1)); // Nome (B1:B2)
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 3)); // Guerra 1 (C1:D1)

            // Definir os cabeçalhos
            Cell cell;
            cell = headerRow1.createCell(0);
            cell.setCellValue("Tag");
            cell.setCellStyle(centeredStyle);

            cell = headerRow1.createCell(1);
            cell.setCellValue("Nome");
            cell.setCellStyle(centeredStyle);

            cell = headerRow1.createCell(2);
            cell.setCellValue("Guerra 1");
            cell.setCellStyle(centeredStyle);

            // Definir sub-cabeçalhos
            cell = headerRow2.createCell(2);
            cell.setCellValue("Ataque");
            cell.setCellStyle(centeredStyle);

            cell = headerRow2.createCell(3);
            cell.setCellValue("Defesa");
            cell.setCellStyle(centeredStyle);

            // Preencher dados da primeira lista
            int rowNum = 2;
            for (ClanWarLeagueWarMembers member : firstMembers) {
                Row row = sheet.createRow(rowNum++);
                Cell cellTag = row.createCell(0);
                cellTag.setCellValue(member.tag());
                cellTag.setCellStyle(leftStyle);

                Cell cellName = row.createCell(1);
                cellName.setCellValue(member.name());

                // Preencher estrelas dos ataques e melhor ataque do oponente
                StringBuilder attackStars = new StringBuilder();
                if (member.attacks() != null) {
                    for (ClanWarLeagueWarAttacks attack : member.attacks()) {
                        attackStars.append(attack.stars()).append(" ");
                    }
                } else {
                    attackStars.append("0");//No attacks found.
                }
                Cell cellAttackStars = row.createCell(2);
                cellAttackStars.setCellValue(attackStars.toString().trim());
                cellAttackStars.setCellStyle(centeredStyle);

                int bestAttackStars = 1; // Valor padrão

                if (member.bestOpponentAttack() != null) {
                    bestAttackStars = 3 - member.bestOpponentAttack().stars();
                }
                Cell cellBestAttackStars = row.createCell(3);
                cellBestAttackStars.setCellValue(bestAttackStars);
                cellBestAttackStars.setCellStyle(centeredStyle);
            }

            // Atualizar cabeçalhos e sub-cabeçalhos para a segunda lista
            int nextColumnGuerra2 = 4; // Inicia a partir da coluna E
            sheet.addMergedRegion(new CellRangeAddress(0, 0, nextColumnGuerra2, nextColumnGuerra2 + 1)); // Nova Guerra 1 (E1:F1)

            // Definir os novos cabeçalhos
            cell = headerRow1.createCell(nextColumnGuerra2);
            cell.setCellValue("Guerra 2");
            cell.setCellStyle(centeredStyle);

            // Definir novos sub-cabeçalhos
            cell = headerRow2.createCell(nextColumnGuerra2);
            cell.setCellValue("Ataque");
            cell.setCellStyle(centeredStyle);

            cell = headerRow2.createCell(nextColumnGuerra2 + 1);
            cell.setCellValue("Defesa");
            cell.setCellStyle(centeredStyle);

            // Preencher dados da segunda lista, começando a partir da nova coluna
            int secondListRowNumGuerra2 = 2;
            for (ClanWarLeagueWarMembers member : secondMembers) {
                Row row = sheet.getRow(secondListRowNumGuerra2++);
                if (row == null) {
                    row = sheet.createRow(secondListRowNumGuerra2 - 1);
                }
                // Apenas preencher "Ataque" e "Defesa" (E e F)
                StringBuilder attackStars = new StringBuilder();
                if (member.attacks() != null) {
                    for (ClanWarLeagueWarAttacks attack : member.attacks()) {
                        attackStars.append(attack.stars()).append(" ");
                    }
                } else {
                    attackStars.append("0"); //No attacks found.
                }
                Cell cellAttackStars = row.createCell(nextColumnGuerra2);
                cellAttackStars.setCellValue(attackStars.toString().trim());
                cellAttackStars.setCellStyle(centeredStyle);

                int bestAttackStars = 1; // Valor padrão

                if (member.bestOpponentAttack() != null) {
                    bestAttackStars = 3 - member.bestOpponentAttack().stars();
                }
                Cell cellBestAttackStars = row.createCell(nextColumnGuerra2 + 1);
                cellBestAttackStars.setCellValue(bestAttackStars);
                cellBestAttackStars.setCellStyle(centeredStyle);
            }

            // Ajustar o tamanho das colunas para caber o conteúdo
            for (int i = 0; i < nextColumnGuerra2 + 2; i++) {
                sheet.autoSizeColumn(i);
            }

            // Atualizar cabeçalhos e sub-cabeçalhos para a terceira lista
            int nextColumnGuerra3 = 6; // Inicia a partir da coluna G
            sheet.addMergedRegion(new CellRangeAddress(0, 0, nextColumnGuerra3, nextColumnGuerra3 + 1)); // Nova Guerra 1 (G1:H1)

            // Definir os novos cabeçalhos
            cell = headerRow1.createCell(nextColumnGuerra3);
            cell.setCellValue("Guerra 3");
            cell.setCellStyle(centeredStyle);

            // Definir novos sub-cabeçalhos
            cell = headerRow2.createCell(nextColumnGuerra3);
            cell.setCellValue("Ataque");
            cell.setCellStyle(centeredStyle);

            cell = headerRow2.createCell(nextColumnGuerra3 + 1);
            cell.setCellValue("Defesa");
            cell.setCellStyle(centeredStyle);

            // Preencher dados da segunda lista, começando a partir da nova coluna
            int secondListRowNumGuerra3 = 2;
            for (ClanWarLeagueWarMembers member : thirdMembers) {
                Row row = sheet.getRow(secondListRowNumGuerra3++);
                if (row == null) {
                    row = sheet.createRow(secondListRowNumGuerra3 - 1);
                }
                // Apenas preencher "Ataque" e "Defesa" (G e H)
                StringBuilder attackStars = new StringBuilder();
                if (member.attacks() != null) {
                    for (ClanWarLeagueWarAttacks attack : member.attacks()) {
                        attackStars.append(attack.stars()).append(" ");
                    }
                } else {
                    attackStars.append("0");//No attacks found.
                }
                Cell cellAttackStars = row.createCell(nextColumnGuerra3);
                cellAttackStars.setCellValue(attackStars.toString().trim());
                cellAttackStars.setCellStyle(centeredStyle);

                int bestAttackStars = 1; // Valor padrão

                if (member.bestOpponentAttack() != null) {
                    bestAttackStars = 3 - member.bestOpponentAttack().stars();
                }
                Cell cellBestAttackStars = row.createCell(nextColumnGuerra3 + 1);
                cellBestAttackStars.setCellValue(bestAttackStars);
                cellBestAttackStars.setCellStyle(centeredStyle);
            }

            // Ajustar o tamanho das colunas para caber o conteúdo
            for (int i = 0; i < nextColumnGuerra3 + 2; i++) {
                sheet.autoSizeColumn(i);
            }


            // Atualizar cabeçalhos e sub-cabeçalhos para a quarta lista
            int nextColumnGuerra4 = 8; // Inicia a partir da coluna I
            sheet.addMergedRegion(new CellRangeAddress(0, 0, nextColumnGuerra4, nextColumnGuerra4 + 1)); // Nova Guerra 1 (I1:J1)

            // Definir os novos cabeçalhos
            cell = headerRow1.createCell(nextColumnGuerra4);
            cell.setCellValue("Guerra 4");
            cell.setCellStyle(centeredStyle);

            // Definir novos sub-cabeçalhos
            cell = headerRow2.createCell(nextColumnGuerra4);
            cell.setCellValue("Ataque");
            cell.setCellStyle(centeredStyle);

            cell = headerRow2.createCell(nextColumnGuerra4 + 1);
            cell.setCellValue("Defesa");
            cell.setCellStyle(centeredStyle);

            // Preencher dados da segunda lista, começando a partir da nova coluna
            int secondListRowNumGuerra4 = 2;
            for (ClanWarLeagueWarMembers member : fourthMembers) {
                Row row = sheet.getRow(secondListRowNumGuerra4++);
                if (row == null) {
                    row = sheet.createRow(secondListRowNumGuerra4 - 1);
                }
                // Apenas preencher "Ataque" e "Defesa" (I e J)
                StringBuilder attackStars = new StringBuilder();
                if (member.attacks() != null) {
                    for (ClanWarLeagueWarAttacks attack : member.attacks()) {
                        attackStars.append(attack.stars()).append(" ");
                    }
                } else {
                    attackStars.append("0");//No attacks found.
                }
                Cell cellAttackStars = row.createCell(nextColumnGuerra4);
                cellAttackStars.setCellValue(attackStars.toString().trim());
                cellAttackStars.setCellStyle(centeredStyle);

                int bestAttackStars = 1; // Valor padrão

                if (member.bestOpponentAttack() != null) {
                    bestAttackStars = 3 - member.bestOpponentAttack().stars();
                }
                Cell cellBestAttackStars = row.createCell(nextColumnGuerra4 + 1);
                cellBestAttackStars.setCellValue(bestAttackStars);
                cellBestAttackStars.setCellStyle(centeredStyle);
            }

            // Ajustar o tamanho das colunas para caber o conteúdo
            for (int i = 0; i < nextColumnGuerra4 + 2; i++) {
                sheet.autoSizeColumn(i);
            }

            // Atualizar cabeçalhos e sub-cabeçalhos para a quinta lista
            int nextColumnGuerra5 = 10; // Inicia a partir da coluna K
            sheet.addMergedRegion(new CellRangeAddress(0, 0, nextColumnGuerra5, nextColumnGuerra5 + 1)); // Nova Guerra 1 (K1:L1)

            // Definir os novos cabeçalhos
            cell = headerRow1.createCell(nextColumnGuerra5);
            cell.setCellValue("Guerra 5");
            cell.setCellStyle(centeredStyle);

            // Definir novos sub-cabeçalhos
            cell = headerRow2.createCell(nextColumnGuerra5);
            cell.setCellValue("Ataque");
            cell.setCellStyle(centeredStyle);

            cell = headerRow2.createCell(nextColumnGuerra5 + 1);
            cell.setCellValue("Defesa");
            cell.setCellStyle(centeredStyle);

            // Preencher dados da segunda lista, começando a partir da nova coluna
            int secondListRowNumGuerra5 = 2;
            for (ClanWarLeagueWarMembers member : fifthMembers) {
                Row row = sheet.getRow(secondListRowNumGuerra5++);
                if (row == null) {
                    row = sheet.createRow(secondListRowNumGuerra5 - 1);
                }
                // Apenas preencher "Ataque" e "Defesa" (K e L)
                StringBuilder attackStars = new StringBuilder();
                if (member.attacks() != null) {
                    for (ClanWarLeagueWarAttacks attack : member.attacks()) {
                        attackStars.append(attack.stars()).append(" ");
                    }
                } else {
                    attackStars.append("0");//No attacks found.
                }
                Cell cellAttackStars = row.createCell(nextColumnGuerra5);
                cellAttackStars.setCellValue(attackStars.toString().trim());
                cellAttackStars.setCellStyle(centeredStyle);

                int bestAttackStars = 1; // Valor padrão

                if (member.bestOpponentAttack() != null) {
                    bestAttackStars = 3 - member.bestOpponentAttack().stars();
                }
                Cell cellBestAttackStars = row.createCell(nextColumnGuerra5 + 1);
                cellBestAttackStars.setCellValue(bestAttackStars);
                cellBestAttackStars.setCellStyle(centeredStyle);
            }

            // Ajustar o tamanho das colunas para caber o conteúdo
            for (int i = 0; i < nextColumnGuerra5 + 2; i++) {
                sheet.autoSizeColumn(i);
            }

            // Atualizar cabeçalhos e sub-cabeçalhos para a sexta lista
            int nextColumnGuerra6 = 12; // Inicia a partir da coluna M
            sheet.addMergedRegion(new CellRangeAddress(0, 0, nextColumnGuerra6, nextColumnGuerra6 + 1)); // Nova Guerra 1 (M1:N1)

            // Definir os novos cabeçalhos
            cell = headerRow1.createCell(nextColumnGuerra6);
            cell.setCellValue("Guerra 6");
            cell.setCellStyle(centeredStyle);

            // Definir novos sub-cabeçalhos
            cell = headerRow2.createCell(nextColumnGuerra6);
            cell.setCellValue("Ataque");
            cell.setCellStyle(centeredStyle);

            cell = headerRow2.createCell(nextColumnGuerra6 + 1);
            cell.setCellValue("Defesa");
            cell.setCellStyle(centeredStyle);

            // Preencher dados da segunda lista, começando a partir da nova coluna
            int secondListRowNumGuerra6 = 2;
            for (ClanWarLeagueWarMembers member : sixthMembers) {
                Row row = sheet.getRow(secondListRowNumGuerra6++);
                if (row == null) {
                    row = sheet.createRow(secondListRowNumGuerra6 - 1);
                }
                // Apenas preencher "Ataque" e "Defesa" (M e N)
                StringBuilder attackStars = new StringBuilder();
                if (member.attacks() != null) {
                    for (ClanWarLeagueWarAttacks attack : member.attacks()) {
                        attackStars.append(attack.stars()).append(" ");
                    }
                } else {
                    attackStars.append("0");//No attacks found.
                }
                Cell cellAttackStars = row.createCell(nextColumnGuerra6);
                cellAttackStars.setCellValue(attackStars.toString().trim());
                cellAttackStars.setCellStyle(centeredStyle);

                int bestAttackStars = 1; // Valor padrão

                if (member.bestOpponentAttack() != null) {
                    bestAttackStars = 3 - member.bestOpponentAttack().stars();
                }
                Cell cellBestAttackStars = row.createCell(nextColumnGuerra6 + 1);
                cellBestAttackStars.setCellValue(bestAttackStars);
                cellBestAttackStars.setCellStyle(centeredStyle);
            }

            // Ajustar o tamanho das colunas para caber o conteúdo
            for (int i = 0; i < nextColumnGuerra6 + 2; i++) {
                sheet.autoSizeColumn(i);
            }

            // Atualizar cabeçalhos e sub-cabeçalhos para a setima lista
            int nextColumnGuerra7 = 14; // Inicia a partir da coluna O
            sheet.addMergedRegion(new CellRangeAddress(0, 0, nextColumnGuerra7, nextColumnGuerra7 + 1)); // Nova Guerra 1 (O1:P1)

            // Definir os novos cabeçalhos
            cell = headerRow1.createCell(nextColumnGuerra7);
            cell.setCellValue("Guerra 7");
            cell.setCellStyle(centeredStyle);

            // Definir novos sub-cabeçalhos
            cell = headerRow2.createCell(nextColumnGuerra7);
            cell.setCellValue("Ataque");
            cell.setCellStyle(centeredStyle);

            cell = headerRow2.createCell(nextColumnGuerra7 + 1);
            cell.setCellValue("Defesa");
            cell.setCellStyle(centeredStyle);

            // Preencher dados da segunda lista, começando a partir da nova coluna
            int secondListRowNumGuerra7 = 2;

            for (ClanWarLeagueWarMembers member : seventhMembers) {
                Row row = sheet.getRow(secondListRowNumGuerra7++);
                if (row == null) {
                    row = sheet.createRow(secondListRowNumGuerra7 - 1);
                }
                // Apenas preencher "Ataque" e "Defesa" (O e P)
                StringBuilder attackStars = new StringBuilder();
                if (member.attacks() != null) {
                    for (ClanWarLeagueWarAttacks attack : member.attacks()) {
                        attackStars.append(attack.stars()).append(" ");
                    }
                } else {
                    attackStars.append("0");//No attacks found.
                }
                Cell cellAttackStars = row.createCell(nextColumnGuerra7);
                if(warInPreparation == 7) {
                    cellAttackStars.setCellValue("0");
                } else {
                    cellAttackStars.setCellValue(attackStars.toString().trim());
                }
                cellAttackStars.setCellStyle(centeredStyle);

                int bestAttackStars = 1; // Valor padrão

//                System.out.println(member.name());
                if(warInPreparation == 7) {
                    bestAttackStars = 0;
                } else {
                    if (member.bestOpponentAttack() != null) {
                        bestAttackStars = 3 - member.bestOpponentAttack().stars();
                    }
                }

                Cell cellBestAttackStars = row.createCell(nextColumnGuerra7 + 1);
                cellBestAttackStars.setCellValue(bestAttackStars);
                cellBestAttackStars.setCellStyle(centeredStyle);
            }

            // Ajustar o tamanho das colunas para caber o conteúdo
            for (int i = 0; i < nextColumnGuerra7 + 2; i++) {
                sheet.autoSizeColumn(i);
            }

            for (int rowIndex = 2; rowIndex < sheet.getLastRowNum() + 1; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }
                double sum = 0;
                for (int colIndex = 2; colIndex <= 15; colIndex++) { // Columns C to P
                    cell = row.getCell(colIndex);
                    if (cell != null) {
                        if (cell.getCellType() == CellType.STRING) {
                            try {
                                double numericValue = Double.parseDouble(cell.getStringCellValue());
                                cell.setCellValue(numericValue);
                            } catch (NumberFormatException e) {
                                // Handle the case where the cell value is not a valid number
                                continue;
                            }
                        }
                        if (cell.getCellType() == CellType.NUMERIC) {
                            sum += cell.getNumericCellValue();
                        }
                    }
                }
                Cell sumCell = row.createCell(16); // Column Q
                sumCell.setCellValue(sum);
                sumCell.setCellStyle(centeredStyle);
            }

            return writeWorkbookToFile(workbook, nomeClan);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private File writeWorkbookToFile(Workbook workbook, String nomeClan) throws IOException {
        File outputDir = new File(System.getProperty("java.io.tmpdir"), "generatedExcels");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        File outputFile = new File(outputDir, "clan_league_" + nomeClan + ".xlsx");
        try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
            workbook.write(fileOut);
        }

        return outputFile;
    }
}

