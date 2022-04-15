//Code authored by Shiva

import java.io.IOException;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

public class Read_Body {
	
	@SuppressWarnings("unused")
	private boolean textIsHtml = false;
	public String getText(Part p) throws MessagingException, IOException {
		try {
		System.setProperty("mail.mime.multipart.ignoreexistingboundaryparameter", "true");

        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            textIsHtml = p.isMimeType("text/html");
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }
		}catch(ClassCastException e) {
			return null;
		}
        return null;
    }
}