package com.zitego.web.menu.button;

import com.zitego.format.FormatType;
import com.zitego.format.UnsupportedFormatException;
import com.zitego.markup.Newline;
import com.zitego.markup.html.HtmlMarkupMap;
import com.zitego.markup.html.tag.Body;
import com.zitego.markup.html.tag.Head;
import com.zitego.markup.html.tag.textEffect.TextEffectType;
import com.zitego.web.menu.Menu;
import org.w3c.dom.Element;

/**
 * This class represents a menu button that is an image. Actions can be set to force
 * a new image on mouse over and/or mouse down events.
 *
 * @author John Glorioso
 * @version $Id: ImageSwappedMenuButton.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 * @see com.zitego.menu.Menu#createButton(MenuButtonType)
 */
public class ImageSwappedMenuButton extends MenuButton
{
    /** The mouse over image src url. */
    private String _mouseOverImageSrc;
    /** The mouse down image src url. */
    private String _mouseDownImageSrc;
    /** The internal image name. */
    private String _imageName;

    /**
     * Creates a new menu button where the images are swapped when the mouse is over
     * it.
     *
     * @param parent The parent menu.
     */
    ImageSwappedMenuButton(Menu parent)
    {
        super(parent);
        setType(MenuButtonType.IMAGE_SWAPPED);
        setImageName( parent.getButtons().size() );
    }

    public void loadWith(MenuButton button)
    {
        super.loadWith(button);
        Menu parent = getMenu();
        setImageName( parent.getButtons().indexOf(this) );

        setButtonDisplay( ButtonDisplayFactory.createButtonDisplay(ButtonDisplayType.IMAGE, getDisplayParent()) );
        ( (ImageButtonDisplay)getButtonDisplay() ).setName(_imageName);

        setIdAttribute(null);
    }

    /**
     * Sets the image name.
     *
     * @param id The id to use for the button.
     */
    private void setImageName(int id)
    {
        _imageName = "menu" + getMenu().getMapId() + "_button" + id + "_img" + getMapId();
    }

    /**
     * Sets the onMouseOver image url.
     *
     * @param src The on mouse over url.
     */
    public void setMouseOverImageUrl(String src)
    {
        _mouseOverImageSrc = src;
        addToOnload();
        ButtonLink link = getLink();
        if (link != null)
        {
            link.setOnMouseOver("swapImage('"+_imageName+"','"+_mouseOverImageSrc+"')");
            link.setOnMouseOut("restoreImage()");
        }
    }

    /**
     * Returns the onMouseOver image url.
     *
     * @return String
     */
    public String getMouseOverImageUrl()
    {
        return _mouseOverImageSrc;
    }

    /**
     * Sets the onMouseDown image url.
     *
     * @param src The on mouse down url.
     */
    public void setMouseDownImageUrl(String src)
    {
        _mouseDownImageSrc = src;
        addToOnload();
        ButtonLink link = getLink();
        if (link != null)
        {
            link.setOnMouseDown("swapImage('"+_imageName+"','"+_mouseDownImageSrc+"')");
            link.setOnMouseDown("restoreImage()");
        }
    }

    /**
     * Returns the onMouseDown image url.
     *
     * @return String
     */
    public String getMouseDownImageUrl()
    {
        return _mouseDownImageSrc;
    }

    /**
     * Overrides parent to throw an exception here.
     *
     * @param label Not used.
     * @return TextButtonDisplay
     * @throws IllegalStateException because swapped image buttons do not have text.
     */
    public TextButtonDisplay createTextDisplay(String label) throws IllegalStateException
    {
        throw new IllegalStateException("Cannot create a text display on a swapped image button");
    }

    /**
     * Overrides parent to throw an exception here.
     *
     * @param effect Not used.
     * @param label Not used.
     * @return TextButtonDisplay
     * @throws IllegalStateException because swapped image buttons do not have text.
     */
    public TextButtonDisplay createTextDisplay(TextEffectType effect, String label) throws IllegalStateException
    {
        throw new IllegalStateException("Cannot create a text display on a swapped image button");
    }

    public ImageButtonDisplay createImageDisplay(String src)
    {
        ImageButtonDisplay d = super.createImageDisplay(src);
        d.setName(_imageName);
        return d;
    }

    public ButtonLink createButtonLink(String url)
    {
        ButtonLink ret = super.createButtonLink(url);
        //TO DO - make this handle on mouse down
        if (_mouseOverImageSrc != null)
        {
            ret.setOnMouseOver("swapImage('"+_imageName+"','"+_mouseOverImageSrc+"')");
            ret.setOnMouseOut("restoreImage()");
        }
        return ret;
    }

    public void registerHeaderTags()
    {
        super.registerHeaderTags();
        addToOnload();
    }

    /**
     * Updates the body tag's onload javascript function. This method relies on the root
     * javascript directory being set. If it was not manually set, then the default is
     * /js/<src> in which case, you will need to make sure that file is present at that
     * path of the server on which this page will be shown.
     */
    private void addToOnload()
    {
        //Add to the pre-load array in the body onload
        Body body = ( (HtmlMarkupMap)getMap() ).getBodyTag();
        if (body != null)
        {
            if (_mouseOverImageSrc != null) body.addImageToPreload(_mouseOverImageSrc);
            if (_mouseDownImageSrc != null) body.addImageToPreload(_mouseDownImageSrc);
        }
        Head h = ( (HtmlMarkupMap)getMap() ).getHeadTag();
        if (h != null) h.registerJsSourceFile(getRootJsDirectory()+"/menu.js");
    }

    public void addChild(Element child)
    {
        super.addChild(child);
        String name = child.getNodeName().toLowerCase();
        if ( name.equals("image_swapped") )
        {
            String val = child.getAttribute("mouseover_image");
            if ( !"".equals(val) )
            {
                setMouseOverImageUrl(val);
            }
            val = child.getAttribute("mousedown_image");
            if ( !"".equals(val) )
            {
                setMouseDownImageUrl(val);
            }
        }
        else if ( name.equals("display") )
        {
            ( (ImageButtonDisplay)getButtonDisplay() ).setName(_imageName);
        }
    }

    public String generateXmlContent() throws UnsupportedFormatException
    {
        StringBuffer ret = new StringBuffer( super.generateXmlContent() );

        //Image swapped element
        ret.append( getPadding(FormatType.XML) ).append(" <image_swapped");
        if (_mouseOverImageSrc != null) ret.append(" mouseover_image=\"").append(_mouseOverImageSrc).append("\"");
        if (_mouseDownImageSrc != null) ret.append(" mousedown_image=\"").append(_mouseDownImageSrc).append("\"");
        ret.append(" />").append(Newline.CHARACTER);
        return ret.toString();
    }
}