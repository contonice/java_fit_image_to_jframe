import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;

public class Frame extends JFrame {
    public static FramePanel panel;

    Frame() {
        //Назначаем окну высоту и ширину
        setBounds(0, 0, 800, 600);
        //Размещаем окно по центру экрана
        setLocationRelativeTo(null);
        //Запрещаем изменять размер окна
        setResizable(false);
        //Назначаем действие при закрытии окна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Назначаем название
        setTitle("Перетащи картинку в это окно!");

        //Создаём JPanel
        panel = new FramePanel();
        //Получаем контейнер окна и прикрепляем туда панель
        Container container = getContentPane();
        container.add(panel);

        //Создаём слушатель событий перетаскивания файлов в окно
        DragNDropListener dragNDropListener = new DragNDropListener();
        //Прикрепляем слушатель к окну
        new DropTarget(this, dragNDropListener);

        //Делаем окно видимым
        setVisible(true);
    }

    static class DragNDropListener implements DropTargetListener {
        @Override
        public void drop(DropTargetDropEvent event) {
            //Разрешаем копирование перетаскиванием
            event.acceptDrop(DnDConstants.ACTION_COPY);

            Transferable transferable = event.getTransferable();
            DataFlavor[] flavors = transferable.getTransferDataFlavors();

            for (DataFlavor flavor : flavors) {
                try {
                    //Если это локальный файл, а не перетянутая из браузера картинка или текст
                    if (flavor.isFlavorJavaFileListType()) {
                        //Получаем список перетянутых файлов
                        java.util.List files = (java.util.List) transferable.getTransferData(flavor);

                        //В случае, если файлов больше одного - сообщаем об ошибке
                        if (files.size() > 1) {
                            JOptionPane.showMessageDialog(new JFrame(),
                                    "Я могу принять только 1 файл за раз",
                                    "Ошибка Drag-N-Drop",
                                    JOptionPane.ERROR_MESSAGE);

                            //Помечаем событие как зафейленое (нужно для ОС)
                            event.dropComplete(false);
                        } else {
                            //Получаем путь к файлу
                            String file = files.get(0).toString();

                            //Отправляем картинку в панель
                            panel.setNewPicture(file);

                            //Помечаем событие как успешное (нужно для ОС)
                            event.dropComplete(true);
                        }
                    } else {
                        JOptionPane.showMessageDialog(new JFrame(),
                                "Это не файл",
                                "Ошибка Drag-N-Drop",
                                JOptionPane.ERROR_MESSAGE);

                        //Помечаем событие как зафейленое (нужно для ОС)
                        event.dropComplete(false);
                        break;
                    }

                } catch (Exception e) {
                    event.dropComplete(false);
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void dragEnter(DropTargetDragEvent event) {}

        @Override
        public void dragOver(DropTargetDragEvent event) {}

        @Override
        public void dropActionChanged(DropTargetDragEvent event) {}

        @Override
        public void dragExit(DropTargetEvent event) {}
    }
}
