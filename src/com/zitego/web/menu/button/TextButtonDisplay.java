package com.zitego.web.menu.button;

import com.zitego.format.*;
import com.zitego.markup.IllegalMarkupException;
import com.zitego.markup.MarkupContent;
import com.zitego.markup.Newline;
import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.markup.html.tag.textEffect.*;
import com.zitego.markup.html.HtmlTextContent;
import org.w3c.dom.*;

/**
 * This is a text menu button display of some flat html text.
 * The text can be surrounded by a text effect as well.
 *
 * @author John Glorioso
 * @version $Id: TextButtonDisplay.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 * @see MenuButton#createTextDisplay(String)
 * @see MenuButton#createTextDisplay(TextEffectType, String)
 */
public class TextButtonDisplay extends HtmlTextContent implements ButtonDisplay
{
    /** The text effect for this display. */
    protected TextEffect _effect;

    /**
     * Creates a new text button display with an html tag parent.
     *
     * @param HtmlMarkupTag The parent tag.
     */
    public TextButtonDisplay(HtmlMarkupTag parent)
    {
        super(parent);
        if (parent instanceof TextEffect) _effect = (TextEffect)parent;
    }

    /**
     * Creates a new text button display with an html tag parent and the specified text.
     *
     * @param HtmlMarkupTag The parent tag.
     * @param String The text to display.
     */
    public TextButtonDisplay(HtmlMarkupTag parent, String text)
    {
        super(parent, text);
        if (parent instanceof TextEffect) _effect = (TextEffect)parent;
    }

    public ButtonDisplayType getType()
    {
        return ButtonDisplayType.TEXT;
    }

    /**
     * Returns whether or not this has a text effect.
     *
     * @return boolean
     */
    public boolean hasTextEffect()
    {
        return (_effect != null);
    }

    public void setParent(HtmlMarkupTag parent)
    {
        if (_effect != null) _effect.setParent(parent);
        else super.setParent(parent);
    }

    /**
     * Returns the text effect for this display.
     *
     * @return TextEffect
     */
    public TextEffect getTextEffect()
    {
        return _effect;
    }

    public void setParent(MarkupContent parent)
    {
        if (_effect != null)
        {
            if (_effect.getParent() != parent) _effect.setParent(parent);
        }
        else
        {
            if (getParent() != parent) super.setParent(parent);
        }
    }

    public MarkupContent getParent()
    {
        if (_effect != null) return _effect.getParent();
        else return super.getParent();
    }

    public MarkupContent getAsMarkupContent()
    {
        if (_effect != null) return _effect;
        else return this;
    }

    /**
     * Sets the text effect for this display.
     *
     * @param TextEffect The effect.
     */
    public void setTextEffect(TextEffect effect)
    {
        if (_effect == effect) return;
        HtmlMarkupTag parent = (HtmlMarkupTag)getParent();
        if (_effect != null) parent.removeBodyContent(_effect);
        if (effect != null)
        {
            if (effect.getParent() != parent) effect.setParent(parent);
            setParent(effect);
        }
        else
        {
            setParent(parent);
        }
        _effect = effect;
    }

    public void buildFromXml(Element root)
    {
        //See if we have a text effect or not
        NodeList nodes = root.getChildNodes();
        int size = nodes.getLength();
        for (int i=0; i<size; i++)
        {
            Node n = nodes.item(i);
            if (n instanceof Element)
            {
                addChild( (Element)n );
            }
            else if (n instanceof Text)
            {
                setText( ((Text)n).getData() );
            }
        }
    }

    public void addChild(Element child)
    {
        String name = child.getNodeName().toLowerCase();
        if ( name.equals("texteffect") )
        {
            setTextEffect
            (
                TextEffectFactory.getTextEffect
                (
                    TextEffectType.evaluate( Integer.parseInt(child.getAttribute("type")) ),
                    (HtmlMarkupTag)getParent()
                )
            );
            //Gotta set the text
            NodeList nodes = child.getChildNodes();
            int size = nodes.getLength();
            for (int i=0; i<size; i++)
            {
                Node n = nodes.item(i);
                if (n instanceof Text)
                {
                    setText( ((Text)n).getData() );
                }
            }
        }
    }

    public void parse(Object objToParse, FormatType type) throws IllegalMarkupException, UnsupportedFormatException
    {
        if (objToParse instanceof Element && type == FormatType.XML) buildFromXml( (Element)objToParse );
        else super.parse(objToParse, type);
    }

    public boolean countDeepness(FormatType type)
    {
        if (getParent() instanceof ButtonLink) return false;
        else return (type == FormatType.XML);
    }

    public String generateContent(FormatType type) throws UnsupportedFormatException
    {
        if (type != FormatType.XML) return super.generateContent(type);

        if ( hasChanged() )
        {
            //Get the padding of this tag, then figure out if we are a child of an effect or a link
            MarkupContent parent = getParent();
            String padding = getPadding(type);
            StringBuffer ret = new StringBuffer()
                .append(padding).append("<display ")
                .append("type=\"").append( getType().getValue() ).append("\">");
            if (_effect != null)
            {
                ret.append("<texteffect ")
                   .append("type=\"").append( _effect.getType().getValue() ).append("\">")
                   .append( getText() ).append("</texteffect>");
            }
            else
            {
                ret.append( getText() );
            }
            ret.append("</display>");
            return ret.toString();
        }
        else
        {
            return (String)getCachedContent(type);
        }
    }
}