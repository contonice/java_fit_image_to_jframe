import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class FramePanel extends JPanel {
    private BufferedImage image;

    FramePanel() {
        //Красим фон окна в черный
        setBackground(Color.BLACK);

        //Создаём таймер для перерисовки окна каждые 300мс
        Timer timerRepaint = new Timer(300, e -> repaint());
        timerRepaint.start();
    }

    public void setNewPicture(String path) {
        try {
            //Принимаем новую картинку и ресайзим её
            BufferedImage img = ImageIO.read(new File(path));

            //Ширина и высота окна
            int bound_width = getWidth();
            int bound_height = getHeight();

            //Ширина и высота картинки
            int original_width = img.getWidth();
            int original_height = img.getHeight();

            //Новая ширина и высота для будущей картинки
            int new_width = original_width;
            int new_height = original_height;

            if (original_width > bound_width) {
                new_width = bound_width;
                new_height = (new_width * original_height) / original_width;
            }
            if (new_height > bound_height) {
                new_height = bound_height;
                new_width = (new_height * original_width) / original_height;
            }

            //Создаём пустой холст с шириной и высотой под размер новой картинки
            BufferedImage resizedImage = new BufferedImage(new_width, new_height, BufferedImage.TYPE_INT_RGB);
            //Получаем объект графики, который нужен, чтобы рисовать на холсте
            Graphics2D graphics2D = resizedImage.createGraphics();
            //Чтобы картинка выглядела лучше, применяем интерполяцию
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            //Рисуем полученную из файла картинку с новой шириной и высотой на пустом холсте
            graphics2D.drawImage(img, 0, 0, new_width, new_height, null);
            image = resizedImage;

        } catch (Exception e) {
            //Произойдет, если файл не является картинкой
            JOptionPane.showMessageDialog(new JFrame(),
                    e.getMessage(),
                    "Ошибка Drag-N-Drop",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void paintComponent(Graphics gr) {
        //МЕТОД ДЛЯ ПЕРЕРИСОВКИ ОКНА

        //Преобразуем стандартный Graphics в Graphics2D,
        // чтобы использовать антиалиасинг
        Graphics2D g2d = (Graphics2D) gr;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //Раскомментируйте, чтобы использовать антиалиасинг для текста
        //g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //Очищаем окно от нарисованных ранее элементов
        super.paintComponent(g2d);

        //Если картинка была скинута в окно - рисуем её
        if (image != null) {
            //Отступы по X и Y нужны для центровки картинки в окне
            g2d.drawImage(image, -(image.getWidth() - getWidth()) / 2, -(image.getHeight() - getHeight()) / 2, null);
        }
    }
}
