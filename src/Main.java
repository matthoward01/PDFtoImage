import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import java.nio.file.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

public class Main {

	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(System.in);
		System.out.println("Folder of pdf's to convert?");
		String inFolder = in.nextLine();
		
		File[] fileList = getFiles(inFolder);
		new File(inFolder + "/Output").mkdirs();
		String outFolder = inFolder + "/Output";
		
		System.out.println("Convert to what Filetype? Ex. 1");
		System.out.println("1. PNG");
		System.out.println("2. JPG");
		String fileType = in.nextLine();
		fileType = fileTypes(fileType);
		System.out.println(fileType);
		System.out.println("What color Space?");
		System.out.println("1. RGB");
		System.out.println("2. GRAYSCALE");		
		String colorSpace = in.nextLine();
		ImageType colorS = colorSpaces(colorSpace);
		System.out.println("What DPI? Ex. 300");
		int dpi = Integer.parseInt(in.nextLine());
		
		for (File f : fileList)
		{
			String fName = f.getName();			
			if(fName.toLowerCase().endsWith(".pdf"))
			{
				System.out.println(f.getName());
				
				convertToIMG(inFolder, f.getCanonicalPath(), fileType, colorS, dpi, outFolder);
			}
		}
		
		
		
		in.close();

	}
	
	private static ImageType colorSpaces(String colorSpace)
	{
		ImageType space = ImageType.RGB;
		if(colorSpace.equals("2"))
		{
			space = ImageType.GRAY;
		}
		return space;
	}
	
	private static String fileTypes(String fileType)
	{
		fileType = ".png";

		if(fileType.equals("2"))
		{
			fileType = ".jpg";
		}
		
		return fileType;
	}
	
	private static File[] getFiles(String path)
	{
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		
		return listOfFiles;
	}
	
	private static void convertToIMG(String inFolder, String pdfFileName, String fileType, ImageType imageType, int dpi, String outFolder) throws IOException
	{
		Path path = Paths.get(pdfFileName);
		PDDocument document = PDDocument.load(new File(pdfFileName));
		PDFRenderer pdfRenderer = new PDFRenderer(document);
		for (int page = 0; page < document.getNumberOfPages(); ++page)
		{ 
		    BufferedImage bim = pdfRenderer.renderImageWithDPI(page, dpi, imageType);

		    System.out.println(path.getFileName() + "-" + (page+1) + " is done...");
		    // suffix in filename will be used as the file format
		    //ImageIO.write(bim, fileType, new File(outFolder + "/" + pdfFileName + "-" + (page+1) + fileType));
		    ImageIOUtil.writeImage(bim, outFolder + "/" + path.getFileName() + "-" + (page+1) + fileType, dpi);
		    //Files.move(Paths.get(pdfFileName + "-" + (page+1) + fileType), Paths.get(outFolder + "/" + pdfFileName + "-" + (page+1) + fileType), StandardCopyOption.REPLACE_EXISTING);
		}
		
		document.close();
		
	}

}
